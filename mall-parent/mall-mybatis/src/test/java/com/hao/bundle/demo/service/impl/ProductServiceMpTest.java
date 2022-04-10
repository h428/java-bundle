package com.hao.bundle.demo.service.impl;

import static org.junit.Assert.*;

import com.hao.bundle.demo.BaseTest;
import com.hao.bundle.demo.pojo.dto.ProductDto;
import com.hao.bundle.demo.pojo.query.PageQuery;
import com.hao.bundle.demo.pojo.query.ProductQuery;
import com.hao.bundle.demo.pojo.query.SortQuery;
import com.hao.bundle.demo.pojo.wrapper.PageBean;
import com.hao.bundle.demo.service.IProductService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductServiceMpTest extends BaseTest {

    @Autowired
    private ProductServiceMp productService;

    @Test
    public void get() {
        // 查询存在的记录
        assertNotNull(productService.get(1L));
        assertNotNull(productService.get(2L));
        assertNotNull(productService.get(11L));

        // 查询不存在的记录
        assertNull(productService.get(1111111L));
    }

    @Test
    public void page() {
        // 根据 name 筛选，按 name 逆序排序的查询
        ProductQuery productQuery = ProductQuery.builder()
            .name("火")
            .build();
        PageQuery pageQuery = PageQuery.builder().page(1).size(2).build();
        SortQuery sortQuery = SortQuery.builder().sort("-name").build();
        PageBean<ProductDto> pageBean = this.productService.page(productQuery, pageQuery, sortQuery);
        assertEquals("小火龙", pageBean.getList().get(0).getName());
        assertEquals("喷火龙", pageBean.getList().get(1).getName());

        // 查询无记录
        productQuery = ProductQuery.builder()
            .name("火2222")
            .build();
        assertEquals(0, this.productService.page(productQuery, pageQuery, sortQuery).getList().size());
    }


}