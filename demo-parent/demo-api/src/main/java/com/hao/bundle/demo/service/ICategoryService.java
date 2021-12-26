package com.hao.bundle.demo.service;

import com.hao.bundle.demo.pojo.dto.CategoryDto;
import java.util.List;

public interface ICategoryService {

    /**
     * 根据 id 查询分类信息
     *
     * @param id 分类 id
     * @return 分类信息
     */
    CategoryDto get(Long id);

    /**
     * 根据 id 查询分类树（递归查询子分类）信息
     *
     * @param id 分类 id
     * @return 分类信息
     */
    CategoryDto getWithChildren(Long id);

    /**
     * 根据 pid 查询下一级子类
     *
     * @param pid 父类 id
     * @return 子类列表
     */
    List<CategoryDto> listChildrenByPid(Long pid);

}
