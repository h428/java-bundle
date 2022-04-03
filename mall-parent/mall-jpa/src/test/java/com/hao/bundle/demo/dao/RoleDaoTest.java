package com.hao.bundle.demo.dao;

import static org.junit.Assert.assertEquals;

import com.hao.bundle.demo.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RoleDaoTest extends BaseTest {

    @Autowired
    private RoleDao roleDao;

    @Test
    public void test() {
        assertEquals(3, this.roleDao.findAll().size());
    }


}