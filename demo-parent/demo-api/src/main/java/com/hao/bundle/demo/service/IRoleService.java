package com.hao.bundle.demo.service;

import com.hao.bundle.demo.pojo.dto.RoleDto;

public interface IRoleService {

    /**
     * 根据 id 查询角色信息（包含权限信息）
     * @param id 角色 id
     * @return 角色信息
     */
    RoleDto get(Long id);

}
