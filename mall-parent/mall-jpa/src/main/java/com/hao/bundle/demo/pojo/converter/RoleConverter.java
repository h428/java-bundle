package com.hao.bundle.demo.pojo.converter;

import com.hao.bundle.demo.entity.Role;
import com.hao.bundle.demo.pojo.dto.RoleDto;
import org.mapstruct.Mapper;

@Mapper
public interface RoleConverter {

    Role dtoToEntity(RoleDto roleDto);

    RoleDto entityToDto(Role role);


}
