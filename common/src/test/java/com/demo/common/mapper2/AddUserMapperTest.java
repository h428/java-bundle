package com.demo.common.mapper2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.demo.common.BaseTest;
import com.demo.common.entity.Perm;
import com.demo.common.entity.Role;
import java.util.List;
import java.util.Set;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

public class AddUserMapperTest extends BaseTest {

    @Autowired
    private AddUserMapper addUserMapper;

    @Test
    public void queryRoleByUserId() {

        // id=1 的用户是 admin
        Role admin = this.addUserMapper.queryRoleByUserId(1L);
        assertNotNull(admin);
        assertEquals("admin", admin.getName());

        // id=2 的用户是 manager
        Role manager = this.addUserMapper.queryRoleByUserId(2L);
        assertNotNull(manager);
        assertEquals("manager", manager.getName());
    }

    @Test
    public void queryPermListByRoleId() {

        // admin 只具有通配符权限
        List<Perm> adminPermList = this.addUserMapper.queryPermListByRoleId(1L);
        assertEquals(1, adminPermList.size());
        assertEquals("*", adminPermList.get(0).getTag());

        List<Perm> managerPermList = this.addUserMapper.queryPermListByRoleId(2L);
        assertEquals(24, managerPermList.size());
    }

    @Test
    public void queryStringPermSetByRoleId() {

        // role=1, admin 的权限 tag set
        Set<String> adminStringPermSet = this.addUserMapper.queryStringPermSetByRoleId(1L);
        assertEquals(1, adminStringPermSet.size());
        assertTrue(adminStringPermSet.contains("*"));
        assertFalse(adminStringPermSet.contains("no perm"));

        // role=2, manager 的权限 tag set
        Set<String> managerStringPermSet = this.addUserMapper.queryStringPermSetByRoleId(2L);
        assertEquals(24, managerStringPermSet.size());
        assertTrue(managerStringPermSet.contains("user:add"));
        assertFalse(managerStringPermSet.contains("no perm"));
        assertFalse(managerStringPermSet.contains("*"));

    }

    @Test
    public void queryPermIdListByRoleId() {
        List<Long> adminPermIdList = this.addUserMapper.queryPermIdListByRoleId(1L, null);
        assertEquals(1, adminPermIdList.size());
        assertEquals(1L, adminPermIdList.get(0).longValue());

        List<Long> managerPermIdList = this.addUserMapper.queryPermIdListByRoleId(2L, null);
        assertEquals(24, managerPermIdList.size());
    }

    @Test
    @Transactional
    @Rollback
    public void insertPermByPidList() {
        // 让 1 插入类别 2, 3, 4, 5 下的共 24 条记录
        assertEquals(24, this.addUserMapper.insertPermByPidList(1L, Lists.newArrayList(2L, 3L, 4L, 5L)));
    }

    @Test
    public void queryStringPermSetByUserId() {

        // role=1, admin 的权限 tag set
        Set<String> adminStringPermSet = this.addUserMapper.queryStringPermSetByUserId(1L, null);
        assertEquals(1, adminStringPermSet.size());
        assertTrue(adminStringPermSet.contains("*"));
        assertFalse(adminStringPermSet.contains("no perm"));


        // role=2, manager 的权限 tag set
        Set<String> managerStringPermSet = this.addUserMapper.queryStringPermSetByUserId(2L, null);
        assertEquals(24, managerStringPermSet.size());
        assertTrue(managerStringPermSet.contains("user:add"));
        assertFalse(managerStringPermSet.contains("no perm"));
        assertFalse(managerStringPermSet.contains("*"));

    }
}