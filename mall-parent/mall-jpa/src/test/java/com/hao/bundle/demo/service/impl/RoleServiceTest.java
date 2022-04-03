package com.hao.bundle.demo.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.hao.bundle.demo.BaseTest;
import com.hao.bundle.demo.service.IRoleService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RoleServiceTest extends BaseTest {

    @Autowired
    private IRoleService roleService;

    @Test
    public void get() {
        assertEquals(27, this.roleService.get(1L).getPermList().size());
        assertEquals(16, this.roleService.get(2L).getPermList().size());
        assertEquals(0, this.roleService.get(3L).getPermList().size());
        assertNull(this.roleService.get(5L));
    }
}