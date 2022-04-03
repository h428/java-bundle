package com.hao.bundle.demo.service.impl;

import com.hao.bundle.demo.entity.Perm;
import com.hao.bundle.demo.entity.Role;
import com.hao.bundle.demo.mapper.PermMapper;
import com.hao.bundle.demo.mapper.RoleMapper;
import com.hao.bundle.demo.pojo.converter.PermConverter;
import com.hao.bundle.demo.pojo.converter.RoleConverter;
import com.hao.bundle.demo.pojo.dto.PermDto;
import com.hao.bundle.demo.pojo.dto.RoleDto;
import com.hao.bundle.demo.service.IRoleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermMapper permMapper;

    @Autowired
    private RoleConverter roleConverter;

    @Autowired
    private PermConverter permConverter;

    @Override
    public RoleDto get(Long id) {
        Role role = this.roleMapper.selectById(id);

        // 进一步查询角色对应的权限
        List<Perm> permList = this.permMapper.findByRoleId(id);
        List<PermDto> permDtoList = this.permConverter.entityToDto(permList);

        RoleDto roleDto = this.roleConverter.entityToDto(role);
        roleDto.setPermList(permDtoList);

        return roleDto;
    }
}
