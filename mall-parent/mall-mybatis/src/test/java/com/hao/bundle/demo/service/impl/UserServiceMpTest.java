package com.hao.bundle.demo.service.impl;

import static org.junit.Assert.*;

import cn.hutool.core.util.RandomUtil;
import com.hao.bundle.demo.BaseTest;
import com.hao.bundle.demo.pojo.dto.UserLoginDto;
import com.hao.bundle.demo.pojo.dto.UserRegisterDto;
import com.hao.bundle.demo.pojo.dto.UserUpdateDto;
import com.hao.bundle.demo.pojo.dto.UserUpdatePasswordDto;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceMpTest extends BaseTest {

    @Autowired
    private UserServiceMp userService;

    @Test
    public void register() {
        UserRegisterDto registerDto = UserRegisterDto.builder()
            .email("ttt@lab.com")
            .userName("goodUser")
            .userPass("aa123456")
            .confirmPass("aa123456")
            .build();
        userService.register(registerDto);
    }

    @Test
    public void loginByEmail() {
        UserLoginDto cat = UserLoginDto.builder()
            .email("cat@hao.com")
            .userPass("cat")
            .build();
        assertNotNull(userService.loginByEmail(cat));

        UserLoginDto errorCat = UserLoginDto.builder()
            .email("cat@hao.com")
            .userPass("catPass")
            .build();
        assertNull(userService.loginByEmail(errorCat));
    }

    @Test
    public void update() {
        assertEquals("cat", userService.get(1L).getUserName());
        UserUpdateDto newCat = UserUpdateDto.builder()
            .id(1L)
            .userName("newCat")
            .build();
        userService.update(newCat);
        assertEquals("newCat", userService.get(1L).getUserName());
    }

    @Test
    public void updatePassword() {
        // 旧密码成功登录
        UserLoginDto cat = UserLoginDto.builder()
            .email("cat@hao.com")
            .userPass("cat")
            .build();
        assertNotNull(userService.loginByEmail(cat));

        // 修改密码
        String newPassword = "cat" + RandomUtil.randomInt(1000);
        UserUpdatePasswordDto updatePasswordDto = UserUpdatePasswordDto.builder()
            .id(1L)
            .oldPass("cat")
            .userPass(newPassword)
            .confirmPass(newPassword)
            .build();
        userService.updatePassword(updatePasswordDto);

        // 旧密码登录失败
        assertNull(userService.loginByEmail(cat));

        // 新密码登录成功
        UserLoginDto newCat = UserLoginDto.builder()
            .email("cat@hao.com")
            .userPass(newPassword)
            .build();
        assertNotNull(userService.loginByEmail(newCat));
    }

    @Test
    public void resetPassword() {
        String password = "cat" + RandomUtil.randomInt(1000);
        UserUpdatePasswordDto passwordDto = UserUpdatePasswordDto.builder()
            .id(1L)
            .userPass(password)
            .confirmPass(password)
            .build();

        this.userService.resetPassword(passwordDto);

        UserLoginDto loginDto = UserLoginDto.builder()
            .email("cat@hao.com")
            .userPass(password)
            .build();

        Assert.assertNotNull(this.userService.loginByEmail(loginDto));
    }

    @Test
    public void get() {
        assertNotNull(this.userService.get(1L));
        assertNotNull(this.userService.get(2L));
        assertNull(this.userService.get(11111L));
    }
}