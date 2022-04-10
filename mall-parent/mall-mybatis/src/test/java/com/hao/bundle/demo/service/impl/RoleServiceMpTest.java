package com.hao.bundle.demo.service.impl;

import static org.junit.Assert.*;

import com.hao.bundle.demo.BaseTest;
import com.hao.bundle.demo.pojo.dto.RoleDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RoleServiceMpTest extends BaseTest {

    @Autowired
    private RoleServiceMp roleService;

    @Test
    public void get() {
        // 查询管理员
        RoleDto admin = this.roleService.get(1L);
        assertNotNull(admin);
        assertEquals("admin", admin.getName());
        // 具备所有权限
        assertEquals(11, admin.getPermList().size());

        // 查询不存在的角色
        assertNull(this.roleService.get(1111L));
    }
}