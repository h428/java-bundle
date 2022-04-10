package com.hao.bundle.demo.service.impl;

import com.hao.bundle.demo.dao.PermDao;
import com.hao.bundle.demo.dao.RoleDao;
import com.hao.bundle.demo.entity.Perm;
import com.hao.bundle.demo.entity.Role;
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
public class RoleServiceJpa implements IRoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermDao permDao;

    private final RoleConverter roleConverter = RoleConverter.INSTANCE;

    private final PermConverter permConverter = PermConverter.INSTANCE;

    @Override
    public RoleDto get(Long id) {
        Role role = this.roleDao.getOne(id);

        // 进一步查询角色对应的权限
        List<Perm> permList = this.permDao.findByRoleId(id);
        List<PermDto> permDtoList = this.permConverter.entityToDto(permList);

        RoleDto roleDto = this.roleConverter.entityToDto(role);
        roleDto.setPermList(permDtoList);

        return roleDto;
    }
}
