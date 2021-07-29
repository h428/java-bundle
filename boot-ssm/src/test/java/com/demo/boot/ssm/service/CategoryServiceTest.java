package com.demo.boot.ssm.service;

import com.demo.boot.ssm.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class CategoryServiceTest extends BaseTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void queryCheckBeforeDelete() {

        // 校验有子类别的 category 不可删除，返回提示信息
        assertNotNull(this.categoryService.queryCheckBeforeDelete(1L));

        // 校验还有商品引用的 category 不可删除，返回提示信息
        assertNotNull(this.categoryService.queryCheckBeforeDelete(101L));


        // 校验正常的可以删除的额 category
        assertNull(this.categoryService.queryCheckBeforeDelete(401L));

        // 校验不存在的 category，可以删除，返回 null
        assertNull(this.categoryService.queryCheckBeforeDelete(10086L));

    }
}