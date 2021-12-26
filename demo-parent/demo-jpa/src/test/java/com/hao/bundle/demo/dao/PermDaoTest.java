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
    public void findAll() {
        assertEquals(27, permDao.findAll().size());
    }

    @Test
    public void save() {
        Long id = 7777777L;
        assertFalse(this.permDao.findById(id).isPresent());

        Perm perm = Perm.builder().name("测试").build();
        perm.setId(id);
        perm.setTag("opr777");
        this.permDao.save(perm);

        assertTrue(this.permDao.findById(id).isPresent());
    }

    @Test
    public void findByRoleId() {
        System.out.println(this.permDao.findByRoleId(1L));
    }
}