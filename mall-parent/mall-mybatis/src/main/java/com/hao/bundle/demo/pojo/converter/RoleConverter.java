package com.hao.bundle.demo.pojo.converter;

import com.hao.bundle.demo.entity.Role;
import com.hao.bundle.demo.pojo.dto.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleConverter {

    RoleConverter INSTANCE = Mappers.getMapper(RoleConverter.class);

    Role dtoToEntity(RoleDto roleDto);

    RoleDto entityToDto(Role role);


}
