package com.demo.boot.ssm.controller;

import com.demo.base.component.entity.User;
import com.demo.base.component.exception.ParamErrorException;
import com.demo.base.component.pojo.bean.ResBean;
import com.demo.base.component.pojo.dto.LoginUserDTO;
import com.demo.base.component.pojo.dto.RegisterUserDTO;
import com.demo.base.component.pojo.vo.LoginSuccessVO;
import com.demo.base.component.util.CodecUtil;
import com.demo.base.component.util.DtoUtil;
import com.demo.base.component.util.JwtUtil;
import com.demo.base.component.util.SecureRandomUtil;
import com.demo.base.component.util.SnowflakeIdWorker;
import com.demo.base.component.util.ValidationUtil;
import com.demo.boot.ssm.bean.thredlocal.UserIdThreadLocal;
import com.demo.boot.ssm.bean.util.EhCacheUtil;
import com.demo.boot.ssm.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("登录、注册相关请求")
public class LoginController {

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private UserService userService;

    @ApiOperation("校验用户名，200 则通过，400 则表示用户名已存在")
    @GetMapping("checkUserName/{userName}")
    public ResponseEntity<ResBean> checkUserName(@PathVariable String userName) {

        if (this.userService.queryExistUserName(userName)) {
            return ResBean.ok_200("用户名可用");
        }
        return ResBean.badRequest_400("用户名已被占用");
    }

    @ApiOperation("校验邮箱，200 则邮箱可用，400 则邮箱已被占用")
    @GetMapping("checkEmail/{email}")
    public ResponseEntity<ResBean> checkEmail(@PathVariable String email) {
        if (!ValidationUtil.emailLegal(email)) {
            return ResBean.badRequest_400("邮箱格式不正确");
        }

        if (this.userService.queryExistEmail(email)) {
            return ResBean.ok_200("邮箱可用");
        }
        return ResBean.badRequest_400("该邮箱已被注册");
    }

    @ApiOperation("用户登录，成功返回 loginToken, refreshToken")
    @PostMapping("login")
    public LoginSuccessVO login(@RequestBody @Validated LoginUserDTO loginUserDto, BindingResult bindingResult) {

        DtoUtil.checkDtoParams(bindingResult);

        // 根据邮箱查询，邮箱唯一
        User user = this.userService.getOneByEntity(User.builder().email(loginUserDto.getEmail()).build());

        // 校验密码
        if (user != null && user.getUserPass().equals(CodecUtil.sha256Hex(loginUserDto.getUserPass(), user.getSalt()))) {
            return generateNewToken(String.valueOf(user.getId()));
        }

        throw new ParamErrorException("用户名或密码错误");
    }


    // 根据 userId 生成新的 token
    private LoginSuccessVO generateNewToken(String userId) {
        // 采用 JWT 作为 loginToken，客户端 token 技术，服务端不存储
        String loginToken = JwtUtil.generateToken(userId);
        // 生成安全随机数作为 refreshToken
        String refreshToken = SecureRandomUtil.getLongHex();

        // 缓存该用户的 refreshToken，用于刷新客户端 token 用
        EhCacheUtil.put(userId, refreshToken);

        // 构造登录成功的返回结果并返回
        return LoginSuccessVO.builder()
                .status(200)
                .loginToken(loginToken)
                .refreshToken(refreshToken)
                .userId(userId)
                .build();
    }

    @ApiOperation("获取新 loginToken 和 refreshToken，返回结果和登录结果保持一致，注意 refreshToken 也会更新")
    @PostMapping("token/refresh")
    public LoginSuccessVO refreshToken(@Validated @RequestBody LoginSuccessVO loginSuccessVO) {

        // 校验 refreshToken ，若 refreshToken 失效则无法刷新，需要重新登录
        if (!StringUtils.equals(EhCacheUtil.get(loginSuccessVO.getUserId()), loginSuccessVO.getRefreshToken())) {
            throw new ParamErrorException("请重新登录");
        }

        // refreshToken 校验通过，重新生成 loginToken 和 refreshToken
        return generateNewToken(loginSuccessVO.getUserId());
    }

    @ApiOperation("校验 token")
    @GetMapping("token/check/{userId}/{loginToken}/{refreshToken}")
    public ResponseEntity<ResBean> checkToken(@PathVariable String userId,
                                              @PathVariable String loginToken,
                                              @PathVariable String refreshToken) {

        // 校验 loginToken
        boolean loginTokenCheckResult = JwtUtil.checkToken(loginToken);

        // 校验 refreshToken
        String refreshTokenCache = EhCacheUtil.get(userId);

        boolean refreshTokenCheckResult = StringUtils.isNotBlank(refreshTokenCache)
                && refreshTokenCache.equals(refreshToken);

        // 根据结果按位组合，两项范围为 0-3
        int res = (refreshTokenCheckResult ? 1 : 0) << 1 | (loginTokenCheckResult ? 1 : 0);

        return ResBean.ok_200(String.valueOf(res));
    }


    @ApiOperation("当前登录用户")
    @GetMapping("current")
    public User current() {
        Long userId = UserIdThreadLocal.get();
        return this.userService.getById(userId);
    }



    // 后台系统一般不提供 register，此处只是单纯实现功能（相当于 add user）
    @ApiOperation("用户注册，发生冲突导致的注册失败返回 409")
    @PostMapping("register")
    public ResponseEntity<ResBean> register(@RequestBody @Validated RegisterUserDTO registerUserDto, BindingResult bindingResult) {
        // 基本参数格式校验
        DtoUtil.checkDtoParams(bindingResult);

        // 注册参数校验（涉及业务）：两次密码是否相同，邮箱是否已注册
        checkRegisterParam(registerUserDto);

        User user = registerUserDto.convertToEntity();
        user.setId(this.snowflakeIdWorker.nextId());
        user.setSalt(CodecUtil.generateSalt256()); // 生成并设置盐值
        user.setUserPass(CodecUtil.sha256Hex(user.getUserPass(), user.getSalt())); // 对原密码加盐hash后保存到实体

        if (this.userService.saveSelective(user) == 1) {
            return ResBean.ok_200("注册成功");
        }

        return ResBean.conflict_409("发生冲突，注册失败");
    }


    // 注册参数校验：涉及业务
    private void checkRegisterParam(RegisterUserDTO registerUserDto) {

        // 两次密码是否相同
        if (!registerUserDto.getUserPass().equals(registerUserDto.getConfirmPass())) {
            throw new ParamErrorException("两次输入的密码不相同");
        }

        // 校验邮箱是否已注册
        if (this.userService.queryExistEmail(registerUserDto.getEmail())) {
            throw new ParamErrorException("该邮箱已经被注册");
        }

        // 校验用户名是否已存在
        if (this.userService.queryExistUserName(registerUserDto.getUserName())) {
            throw new ParamErrorException("该用户名已被使用");
        }

    }

    @GetMapping("perms")
    public Set<String> perms() {
        Long userId = UserIdThreadLocal.get();
        return this.userService.queryStringPermission(userId);
    }
}
