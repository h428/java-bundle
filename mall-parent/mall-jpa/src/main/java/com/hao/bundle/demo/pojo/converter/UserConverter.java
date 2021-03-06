package com.hao.bundle.demo.pojo.converter;

import com.hao.bundle.demo.pojo.bo.UserBo;
import com.hao.bundle.demo.entity.User;
import com.hao.bundle.demo.pojo.dto.UserDto;
import com.hao.bundle.demo.pojo.dto.UserRegisterDto;
import com.hao.bundle.demo.pojo.dto.UserUpdatePasswordDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    User dtoToEntity(UserRegisterDto baseUserRegisterDto);

    UserDto entityToDto(User baseUser);

    UserBo entityToBo(User baseUser);

    UserBo updatePasswordDtoToBo(UserUpdatePasswordDto baseUserUpdatePasswordDto);

}
