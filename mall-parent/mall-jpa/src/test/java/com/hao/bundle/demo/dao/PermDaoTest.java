package com.hao.bundle.demo.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.hao.bundle.demo.BaseTest;
import com.hao.bundle.demo.entity.Perm;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PermDaoTest extends BaseTest {

    @Autowired
    private PermDao permDao;

    @Test
    public void findByRoleId() {
        // 管理员角色 admin 具有所有权限，共 11 条记录
        assertEquals(11, this.permDao.findByRoleId(1L).size());

        // 主管角色 manager 具有出权限管理之外的所有权限，共 8 条记录
        assertEquals(8, this.permDao.findByRoleId(2L).size());

        // 成员角色 member 无额外授权，共 0 条记录
        assertEquals(0, this.permDao.findByRoleId(3L).size());

        // 不存在的角色，0 条记录
        assertEquals(0, this.permDao.findByRoleId(1111111111111L).size());
    }
}