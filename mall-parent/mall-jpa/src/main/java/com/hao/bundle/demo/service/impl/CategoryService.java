package com.hao.bundle.demo.service.impl;

import cn.hutool.core.lang.Assert;
import com.hao.bundle.demo.dao.CategoryDao;
import com.hao.bundle.demo.entity.Category;
import com.hao.bundle.demo.pojo.converter.CategoryConverter;
import com.hao.bundle.demo.pojo.dto.CategoryDto;
import com.hao.bundle.demo.service.ICategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CategoryConverter categoryConverter;

    @Override
    public CategoryDto get(Long id) {
        Category category = this.categoryDao.getOne(id);
        return this.categoryConverter.entityToDto(category);
    }

    @Override
    public CategoryDto getWithChildren(Long id) {
        Category category = this.categoryDao.getOne(id);
        CategoryDto categoryDto = this.categoryConverter.entityToDto(category);

        // 递归查询子树
        this.findChildrenRecurrent(categoryDto);

        return categoryDto;
    }

    @Override
    public List<CategoryDto> listChildrenByPid(Long pid) {
        List<Category> children = this.categoryDao.findByPid(pid);
        return this.categoryConverter.entityToDto(children);
    }

    /**
     * CategoryDto 已经查询出信息，递归查询 children 信息
     *
     * @param categoryDto 已查询出的 categoryDto
     */
    private void findChildrenRecurrent(CategoryDto categoryDto) {
        Assert.notNull(categoryDto);

        Long id = categoryDto.getId(); // 作为 pid 查询子树

        // 查询当前对象的下一层类别，并设置子类别
        List<CategoryDto> children = this.listChildrenByPid(id);
        categoryDto.setChildren(children);

        // 各个子类别已经查询出来，递归插叙子类别的子类别
        if (children != null && !children.isEmpty()) {
            children.forEach(this::findChildrenRecurrent);
        }
    }
}
