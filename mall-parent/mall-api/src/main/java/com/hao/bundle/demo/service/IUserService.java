package com.hao.bundle.demo.service;

import com.hao.bundle.demo.pojo.dto.UserDto;
import com.hao.bundle.demo.pojo.dto.UserLoginDto;
import com.hao.bundle.demo.pojo.dto.UserRegisterDto;
import com.hao.bundle.demo.pojo.dto.UserUpdateDto;
import com.hao.bundle.demo.pojo.dto.UserUpdatePasswordDto;

public interface IUserService {

    void register(UserRegisterDto userRegisterDto);

    UserDto loginByEmail(UserLoginDto userLoginDto);

    void update(UserUpdateDto userUpdateDto);

    void updatePassword(UserUpdatePasswordDto passwordDto);

    void resetPassword(UserUpdatePasswordDto passwordDto);

    UserDto get(Long id);

}
