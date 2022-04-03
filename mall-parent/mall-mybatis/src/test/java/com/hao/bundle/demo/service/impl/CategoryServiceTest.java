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
    }

    @Test
    public void getWithChildren() {
        System.out.println(this.categoryService.getWithChildren(1L));
    }

    @Test
    public void listChildrenByPid() {
        System.out.println(this.categoryService.listChildrenByPid(1L));
    }
}