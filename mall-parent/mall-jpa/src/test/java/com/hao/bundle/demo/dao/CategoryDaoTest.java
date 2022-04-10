package com.hao.bundle.demo.dao;

import static org.junit.Assert.*;

import com.hao.bundle.demo.BaseTest;
import com.hao.bundle.demo.entity.Category;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryDaoTest extends BaseTest {

    @Autowired
    private CategoryDao categoryDao;

    @Test
    public void findByPid() {
        assertEquals(4, this.categoryDao.findByPid(1L).size());
        assertEquals(6, this.categoryDao.findByPid(2L).size());
        assertEquals(0, this.categoryDao.findByPid(101L).size());
        assertEquals(0, this.categoryDao.findByPid(11111L).size());
    }

    @Test
    public void existChildrenByPid() {
        // 存在父分类 pid 为指定 id 的情况
        assertTrue(categoryDao.existsByPid(1L));

        // pid 记录自身存在，但不存在其他子分类的 pid 为指定 pid 的情况
        assertFalse(categoryDao.existsByPid(104L));

        // pid 记录自身不存在，且更无其他子分类的 pid 为指定 pid 的情况
        assertFalse(categoryDao.existsByPid(1111111111L));
    }


}