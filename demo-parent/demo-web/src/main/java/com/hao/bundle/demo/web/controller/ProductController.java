package com.hao.bundle.demo.web.controller;

import com.hao.bundle.demo.common.aop.anno.Perm;
import com.hao.bundle.demo.pojo.constant.PermTag;
import com.hao.bundle.demo.pojo.dto.ProductDto;
import com.hao.bundle.demo.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping("{id}")
    @Perm(PermTag.PRODUCT)
    public ProductDto get(@PathVariable Long id) {
        return this.productService.get(id);
    }

}
