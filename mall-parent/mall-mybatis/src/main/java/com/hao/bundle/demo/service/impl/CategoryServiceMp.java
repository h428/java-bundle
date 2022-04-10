package com.hao.bundle.demo.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
import com.hao.bundle.demo.common.exception.BusinessException;
import com.hao.bundle.demo.entity.Category;
import com.hao.bundle.demo.mapper.CategoryMapper;
import com.hao.bundle.demo.pojo.converter.CategoryConverter;
import com.hao.bundle.demo.pojo.dto.CategoryDto;
import com.hao.bundle.demo.pojo.dto.CategorySaveDto;
import com.hao.bundle.demo.service.ICategoryService;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryServiceMp extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    private final CategoryConverter categoryConverter = CategoryConverter.INSTANCE;

    /**
     * 从 pid 开始往上递归查找分类路径，并从顶层逐一放置分类 id 到 set
     * @param pid 开始查询的 pid
     * @return 分类 id 构成的 set
     */
    public Set<Long> findPidSetByPid(Long pid) {

        if (pid == 0L) {
            return Sets.newLinkedHashSet();
        }

        Category category = this.getById(pid);

        Set<Long> parentSet = findPidSetByPid(category.getPid());
        parentSet.add(pid);

        return parentSet;
    }

    @Override
    public void save(CategorySaveDto categorySaveDto) {

        Long id = categorySaveDto.getId();

        boolean isUpdate = id != null;

        if (isUpdate) {
            // 对于更新操作，校验 id 合法
            if (!this.categoryMapper.existById(id)) {
                throw new BusinessException(String.format("分类【%d】不存在", id));
            }
        }

        // 不管是更新还是新增操作，都要校验 pid 是否合法
        Long pid = categorySaveDto.getPid();

        if (0 != pid && !this.categoryMapper.existById(pid)) {
            throw new BusinessException(String.format("父分类【%d】不存在", pid));
        }

        // 对于更新操作，还需要校验 pid 未构成循环依赖
        if (isUpdate) {
            Set<Long> pidSet = findPidSetByPid(pid);
            if (pidSet.contains(id)) {
                throw new BusinessException(String.format("父分类【%d】设置有误，分类构成循环依赖", pid));
            }
        }

        Category category = this.categoryConverter.saveDtoToEntity(categorySaveDto);
        this.saveOrUpdate(category);
        categorySaveDto.setId(category.getId());
    }

    @Override
    public void delete(Long id) {
        // 校验该分类是否存在子类
        if (this.categoryMapper.existChildrenByPid(id)) {
            throw new BusinessException(String.format("分类【%d】存在子分类，无法删除", id));
        }

        // todo 校验分类是否被引用

        if (!this.removeById(id)) {
            throw new BusinessException(String.format("分类【%d】不存在", id));
        }
    }

    @Override
    public boolean existById(Long id) {
        return this.categoryMapper.existById(id);
    }

    @Override
    public CategoryDto get(Long id) {
        Category category = this.categoryMapper.selectById(id);
        return this.categoryConverter.entityToDto(category);
    }

    @Override
    public CategoryDto getWithChildren(Long id) {
        Category category = this.categoryMapper.selectById(id);
        CategoryDto categoryDto = this.categoryConverter.entityToDto(category);

        // 递归查询子树
        this.findChildrenRecurrent(categoryDto);

        return categoryDto;
    }

    @Override
    public List<CategoryDto> listChildrenByPid(Long pid) {
        LambdaQueryWrapper<Category> wrapper = Wrappers.lambdaQuery(
            Category.builder().pid(pid).build());
        List<Category> children = this.categoryMapper.selectList(wrapper);
        return this.categoryConverter.entityToDto(children);
    }

    /**
     * CategoryDto 已经查询出信息，递归查询 children 信息
     *
     * @param categoryDto 已查询出的 categoryDto
     */
    private void findChildrenRecurrent(CategoryDto categoryDto) {
        if (categoryDto == null) {
            return;
        }

        // 作为 pid 查询子树
        Long id = categoryDto.getId();

        // 查询当前对象的下一层类别，并设置子类别
        List<CategoryDto> children = this.listChildrenByPid(id);
        categoryDto.setChildren(children);

        // 各个子类别已经查询出来，递归插叙子类别的子类别
        if (children != null && !children.isEmpty()) {
            children.forEach(this::findChildrenRecurrent);
        }
    }
}
