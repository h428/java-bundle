package com.hao.bundle.demo.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.hao.bundle.demo.BaseTest;
import com.hao.bundle.demo.entity.RolePerm;
import com.hao.bundle.demo.entity.RolePerm.RolePermId;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RolePermDaoTest extends BaseTest {

    @Autowired
    private RolePermDao rolePermDao;

    @Test
    public void getOne() {
        assertNotNull(this.rolePermDao.getOne(RolePermId.builder().roleId(1L).permId(1L).build()));
    }

    @Test
    public void findAll() {
        assertEquals(44, this.rolePermDao.findAll().size());
    }

    @Test
    public void save() {
        RolePerm rolePerm = RolePerm.builder().roleId(77777L).permId(88888L).build();
        this.rolePermDao.save(rolePerm);
    }

}