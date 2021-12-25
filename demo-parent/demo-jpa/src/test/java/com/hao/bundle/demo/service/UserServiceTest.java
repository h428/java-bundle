package com.hao.bundle.demo.service;

import cn.hutool.core.util.RandomUtil;
import com.hao.bundle.demo.BaseTest;
import com.hao.bundle.demo.dao.UserDao;
import com.hao.bundle.demo.pojo.dto.UserLoginDto;
import com.hao.bundle.demo.pojo.dto.UserRegisterDto;
import com.hao.bundle.demo.pojo.dto.UserUpdateDto;
import com.hao.bundle.demo.pojo.dto.UserUpdatePasswordDto;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends BaseTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Test
    public void register() {

        String email = "Lyinghao2@126.com";
        Assert.assertNull(userDao.getByEmail(email));

        UserRegisterDto registerDTO = UserRegisterDto.builder()
            .email(email)
            .userName("lyh222")
            .userPass("aa123456")
            .confirmPass("aa123456")
            .build();
        userService.register(registerDTO);

        Assert.assertNotNull(userDao.getByEmail(email));
    }

    @Test
    public void loginByEmail() {

        // 不存在的用户登录
        UserLoginDto userLoginDto = UserLoginDto.builder()
            .email("Lyinghao@126.com")
            .userPass("aa123456")
            .build();
        Assert.assertNull(userService.loginByEmail(userLoginDto));

        // 存在的用户登录
        UserLoginDto cat = UserLoginDto.builder()
            .email("cat@hao.com")
            .userPass("cat")
            .build();

        Assert.assertNotNull(userService.loginByEmail(cat));

    }

    @Test
    public void update() {
        UserUpdateDto updateDto = UserUpdateDto.builder()
            .id(1L)
            .userName("ttt")
            .avatar("https://123.com")
            .build();
        userService.update(updateDto);

    }

    @Test
    public void updatePassword() {
        String password = "cat" + RandomUtil.randomInt(1000);
        UserUpdatePasswordDto passwordDto = UserUpdatePasswordDto.builder()
            .id(1L)
            .oldPass("cat")
            .userPass(password)
            .confirmPass(password)
            .build();

        this.userService.updatePassword(passwordDto);

        UserLoginDto loginDto = UserLoginDto.builder()
            .email("cat@hao.com")
            .userPass(password)
            .build();

        Assert.assertNotNull(this.userService.loginByEmail(loginDto));

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
}