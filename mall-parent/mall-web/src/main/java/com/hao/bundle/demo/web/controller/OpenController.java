package com.hao.bundle.demo.web.controller;

import com.google.common.collect.Lists;
import com.hao.bundle.demo.common.component.TokenUtil;
import com.hao.bundle.demo.pojo.wrapper.PageBean;
import com.hao.bundle.demo.pojo.wrapper.ResBean;
import com.hao.bundle.demo.common.util.EntityUtil;
import com.hao.bundle.demo.entity.User;
import com.hao.bundle.demo.pojo.dto.UserDto;
import com.hao.bundle.demo.pojo.dto.UserLoginDto;
import com.hao.bundle.demo.pojo.vo.LoginResultVo;
import com.hao.bundle.demo.service.impl.UserServiceMp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/open")
public class OpenController {

    @Autowired
    private UserServiceMp userServiceMp;

    @PostMapping("login")
    public ResBean<LoginResultVo> login(@RequestBody @Validated UserLoginDto userLoginDto) {
        // todo captcha check
        UserDto user = this.userServiceMp.loginByEmail(userLoginDto);

        if (user == null) {
            return ResBean.badRequest_400("用户名或密码错误，登录失败");
        }

        String token = TokenUtil.generateTokenOfBaseUser(user.getId());

        LoginResultVo loginResult = LoginResultVo.builder()
            .user(user)
            .token(token)
            .build();

        return ResBean.ok_200(loginResult);
    }

    @GetMapping("testList")
    public ResBean<List<User>> testList() {

        List<User> users = Lists.newArrayList();
        for (int i = 0; i < 5; i++) {
            User t = EntityUtil.generateRandomOne(User.class, i);
            users.add(t);
        }

        return ResBean.ok_200(users);
    }

    @GetMapping("testPage")
    public ResBean<PageBean<User>> testPage() {

        List<User> baseUsers = Lists.newArrayList();
        for (int i = 0; i < 5; i++) {
            User t = EntityUtil.generateRandomOne(User.class, i);
            baseUsers.add(t);
        }

        PageBean<User> pageBeanBean = PageBean.<User>builder().list(baseUsers).build();

        return ResBean.ok_200(pageBeanBean);
    }



}
