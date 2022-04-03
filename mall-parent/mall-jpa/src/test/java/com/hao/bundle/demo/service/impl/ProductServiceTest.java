package com.hao.bundle.demo.service.impl;

import static org.junit.Assert.*;

import com.hao.bundle.demo.BaseTest;
import com.hao.bundle.demo.common.util.EntityUtil;
import com.hao.bundle.demo.entity.Product;
import com.hao.bundle.demo.pojo.dto.ProductDto;
import com.hao.bundle.demo.pojo.query.PageQuery;
import com.hao.bundle.demo.pojo.query.ProductQuery;
import com.hao.bundle.demo.pojo.wrapper.Page;
import com.hao.bundle.demo.service.ICategoryService;
import com.hao.bundle.demo.service.IProductService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductServiceTest extends BaseTest {

    @Autowired
    private IProductService productService;

    @Test
    public void page() {

        ProductQuery productQuery = ProductQuery.builder()
            .name("火")
            .build();

        Page<ProductDto> page = this.productService.page(productQuery, PageQuery.builder().build());

        System.out.println(page);

    }
}