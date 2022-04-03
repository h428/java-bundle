package com.hao.bundle.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hao.bundle.demo.common.exception.ParamErrorException;
import com.hao.bundle.demo.entity.User;
import com.hao.bundle.demo.mapper.UserMapper;
import com.hao.bundle.demo.pojo.bo.UserBo;
import com.hao.bundle.demo.pojo.converter.UserConverter;
import com.hao.bundle.demo.pojo.dto.UserDto;
import com.hao.bundle.demo.pojo.dto.UserLoginDto;
import com.hao.bundle.demo.pojo.dto.UserRegisterDto;
import com.hao.bundle.demo.pojo.dto.UserUpdateDto;
import com.hao.bundle.demo.pojo.dto.UserUpdatePasswordDto;
import com.hao.bundle.demo.service.IUserService;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserConverter userConverter;

    @Override
    public void register(UserRegisterDto userRegisterDto) {

        userRegisterDto.validatePassword();

        // 转换为 baseUser 并生成对应的 salt 和 password
        User user = this.userConverter.dtoToEntity(userRegisterDto);
        UserBo baseUserBo = this.userConverter.entityToBo(user);
        // 生成密码
        baseUserBo.generateSaltAndPassword();
        // 拷贝处理后的业务参数
        BeanUtil.copyProperties(baseUserBo, user);

        // 设置其他参数
        LocalDateTime now = LocalDateTime.now();
        user.setCreateTime(now);

        this.userMapper.insert(user);
    }


    @Override
    public UserDto loginByEmail(UserLoginDto userLoginDto) {

        String email = userLoginDto.getEmail();

        Assert.notEmpty(email);

        // 根据邮箱查询对象
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery()
            .eq(User::getEmail, email);
        User userByEmail = this.userMapper.selectOne(wrapper);

        // 若邮箱未注册，直接返回 null 表示登录失败
        if (userByEmail == null) {
            return null;
        }

        // 校验输入密码是否正确
        UserBo userBo = this.userConverter.entityToBo(userByEmail);
        if (userBo.passwordValid(userLoginDto.getUserPass())) {
            // 密码正确则返回查询出的对象
            return this.userConverter.entityToDto(userByEmail);
        }

        // 密码错误，登录失败
        return null;
    }

    @Override
    public void update(UserUpdateDto userUpdateDto) {

        Long id = userUpdateDto.getId();
        Assert.notNull(id);

        User user = this.userMapper.selectById(id);
        Assert.notNull(user);


        String userName = userUpdateDto.getUserName();

        if (StrUtil.isNotEmpty(userName) && !userName.equals(user.getUserName())) {
            // 确认用户名不被占用
            LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery()
                .eq(User::getUserName, userName);
            User byUserName = userMapper.selectOne(wrapper);
            if (byUserName != null) {
                throw new ParamErrorException("用户名已被占用");
            }
            user.setUserName(userName);
        }

        LocalDateTime now = LocalDateTime.now();
        user.setUpdateTime(now);

        CopyOptions copyOptions = CopyOptions.create().ignoreNullValue().setIgnoreProperties("id", "userName");
        BeanUtil.copyProperties(userUpdateDto, user, copyOptions);
    }

    @Override
    public void updatePassword(UserUpdatePasswordDto passwordDto) {
        // 声明必须参数不为空
        Assert.notNull(passwordDto.getId());
        Assert.notEmpty(passwordDto.getOldPass());
        Assert.notEmpty(passwordDto.getUserPass());
        Assert.notEmpty(passwordDto.getConfirmPass());

        // 校验密码符合业务条件
        passwordDto.checkPasswordBusinessValid();

        // 查询用户并验证旧密码
        User baseUser = this.userMapper.selectById(passwordDto.getId());
        UserBo userBoForPasswordCheck = this.userConverter.entityToBo(baseUser);

        if (!userBoForPasswordCheck.passwordValid(passwordDto.getOldPass())) {
            throw new ParamErrorException("旧密码验证不通过");
        }

        // 使用 Bo 生成新密码
        UserBo userBoForUpdatePassword = this.userConverter.updatePasswordDtoToBo(passwordDto);
        userBoForUpdatePassword.generateSaltAndPassword();

        // 更新密码到 entity
        baseUser.setUserPass(userBoForUpdatePassword.getUserPass());
        baseUser.setSalt(userBoForUpdatePassword.getSalt());
        baseUser.setUpdateTime(LocalDateTime.now());
    }

    @Override
    public void resetPassword(UserUpdatePasswordDto passwordDto) {
        // 声明必须参数不为空
        Assert.notNull(passwordDto.getId());
        Assert.notEmpty(passwordDto.getUserPass());
        Assert.notEmpty(passwordDto.getConfirmPass());

        // 校验密码符合业务条件
        passwordDto.checkPasswordBusinessValid();

        User user = this.userMapper.selectById(passwordDto.getId());

        // 使用 Bo 生成新密码
        UserBo baseUserBoForUpdatePassword = this.userConverter.updatePasswordDtoToBo(passwordDto);
        baseUserBoForUpdatePassword.generateSaltAndPassword();

        // 更新密码到 entity
        user.setUserPass(baseUserBoForUpdatePassword.getUserPass());
        user.setSalt(baseUserBoForUpdatePassword.getSalt());
        user.setUpdateTime(LocalDateTime.now());
    }

    @Override
    public UserDto get(Long id) {
        User one = this.userMapper.selectById(id);
        return this.userConverter.entityToDto(one);
    }


}
