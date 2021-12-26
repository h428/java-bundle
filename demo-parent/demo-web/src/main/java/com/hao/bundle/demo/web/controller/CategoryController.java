package com.hao.bundle.demo.web.controller;

import com.hao.bundle.demo.common.aop.anno.Perm;
import com.hao.bundle.demo.pojo.constant.PermTag;
import com.hao.bundle.demo.pojo.dto.CategoryDto;
import com.hao.bundle.demo.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("{id}")
    @Perm(PermTag.CATEGORY)
    public CategoryDto get(@PathVariable Long id) {
        return this.categoryService.get(id);
    }

}
