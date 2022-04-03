package com.hao.bundle.demo.service.impl;

import static org.junit.Assert.*;

import com.hao.bundle.demo.BaseTest;
import com.hao.bundle.demo.service.ICategoryService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryServiceTest extends BaseTest {

    @Autowired
    private ICategoryService categoryService;

    @Test
    public void get() {
        assertNotNull(this.categoryService.get(1L));
        assertNull(this.categoryService.get(1111L));
    }

    @Test
    public void getWithChildren() {
    }

    @Test
    public void listChildrenByPid() {
    }
}