package com.demo.boot.ssm.service;

import com.demo.base.component.base.BaseService;
import com.demo.base.component.entity.Role;
import com.demo.base.component.entity.RolePerm;
import com.demo.base.component.mapper.PermMapper;
import com.demo.base.component.mapper.RolePermMapper;
import com.demo.base.component.mapper2.AddUserMapper;
import com.demo.base.component.pojo.dto.RoleDTO;
import com.demo.base.component.util.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService extends BaseService<Role> {

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Autowired
    private RolePermMapper rolePermMapper;

    @Autowired
    private PermMapper permMapper;

    @Autowired
    private AddUserMapper addUserMapper;


    // 保存该角色授权信息
    private int saveRolePerm(Long roleId, List<Long> permIdList) {

        if (permIdList != null && permIdList.size() > 0) {
            return this.addUserMapper.insertPermByPidList(roleId, permIdList);
        }

        return 0;
    }

    // 删除该角色授权信息
    private int deleteRolePerm(Long roleId) {
        return this.rolePermMapper.delete(RolePerm.builder().roleId(roleId).build());
    }

    /**
     * 删除该角色，同时删除授权信息
     * @param roleId
     * @return
     */
    public int deleteRoleWithPerm(Long roleId) {
        deleteRolePerm(roleId);
        return this.mapper.deleteByPrimaryKey(roleId);
    }


    /**
     * 给角色授权
     * @param roleId 角色 id
     * @param permIdList 权限列表
     * @return 授权数量
     */
    public int updateRolePerm(Long roleId, List<Long> permIdList) {
        // 删除后再插入
        deleteRolePerm(roleId);
        return saveRolePerm(roleId, permIdList);
    }

    public RoleDTO getRoleWithPermId(Long roleId) {

        // 查询
        Role role = this.mapper.selectByPrimaryKey(roleId);

        // role 为空则直接返回 null，不用再查询
        if (role == null) {
            return null;
        }


        // 继续查询出 permIdList
        List<Long> permIdList = this.addUserMapper.queryPermIdListByRoleId(roleId, true);

        return RoleDTO.builder()
                .id(roleId)
                .name(role.getName())
                .permIdList(permIdList)
                .build();
    }


}
