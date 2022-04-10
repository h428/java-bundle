package com.hao.bundle.demo.service.impl;

import static org.junit.Assert.*;

import com.hao.bundle.demo.BaseTest;
import com.hao.bundle.demo.common.exception.BusinessException;
import com.hao.bundle.demo.common.util.EntityUtil;
import com.hao.bundle.demo.pojo.dto.CategoryDto;
import com.hao.bundle.demo.pojo.dto.CategorySaveDto;
import com.hao.bundle.demo.service.ICategoryService;
import java.util.Set;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

public class CategoryServiceMpTest extends BaseTest {

    @Autowired
    private CategoryServiceMp categoryService;

    @Test
    public void findPidSetByPid() {
        assertEquals(1, categoryService.findPidSetByPid(1L).size());
        assertEquals(2, categoryService.findPidSetByPid(403L).size());
    }

    @Test
    public void save() {
        // 新增记录
        CategorySaveDto categoryAddDto = EntityUtil.generateRandomOne(CategorySaveDto.class);
        categoryAddDto.setId(null);
        categoryAddDto.setPid(2L);
        categoryService.save(categoryAddDto);
        // 确认 id 回写
        assertNotNull(categoryAddDto.getId());
        // 确认对应记录存在
        assertNotNull(this.categoryService.get(categoryAddDto.getId()));

        // 更新记录，先查询旧值判断旧值
        CategoryDto categoryDto = this.categoryService.get(1L);
        assertEquals("经典系", categoryDto.getName());
        assertEquals(0L, categoryDto.getPid().longValue());

        // 更新操作
        CategorySaveDto categoryUpdateDto = EntityUtil.generateRandomOne(CategorySaveDto.class);
        categoryUpdateDto.setId(1L);
        categoryUpdateDto.setPid(0L);
        this.categoryService.save(categoryUpdateDto);
        // 查询新值确认值被修改
        categoryDto = this.categoryService.get(1L);
        assertEquals(categoryDto.getName(), categoryUpdateDto.getName());
        assertEquals(categoryDto.getPid(), categoryUpdateDto.getPid());

        // 修改一个 id 不存在的记录
        categoryUpdateDto = EntityUtil.generateRandomOne(CategorySaveDto.class);
        categoryUpdateDto.setId(111111L);
        try {
            this.categoryService.save(categoryUpdateDto);
        } catch (Exception e) {
            assertTrue(e instanceof BusinessException);
        }

    }

    @Test
    public void delete() {
        assertTrue(this.categoryService.existById(1L));
        assertTrue(this.categoryService.existById(101L));
        this.categoryService.delete(101L);
        assertFalse(this.categoryService.existById(101L));
        this.categoryService.delete(102L);
        this.categoryService.delete(103L);
        this.categoryService.delete(104L);
        this.categoryService.delete(1L);
        assertFalse(this.categoryService.existById(1L));
    }


    @Test
    public void existById() {
        assertTrue(this.categoryService.existById(1L));
        assertTrue(this.categoryService.existById(101L));
        assertFalse(this.categoryService.existById(111111L));
    }

    @Test
    public void get() {
        CategoryDto categoryDto = this.categoryService.get(1L);
        assertNotNull(categoryDto);
        assertEquals("经典系", categoryDto.getName());
        assertNotNull(this.categoryService.get(101L));
        assertNull(this.categoryService.get(1111111L));
    }

    @Test
    public void getWithChildren() {
        // 查询包含子分类的分类
        CategoryDto categoryDto = this.categoryService.getWithChildren(1L);
        assertNotNull(categoryDto);
        assertNotNull(categoryDto.getChildren());
        assertEquals(4, categoryDto.getChildren().size());

        // 查询不包含子分类的分类
        categoryDto = this.categoryService.getWithChildren(101L);
        assertNotNull(categoryDto);
        assertNotNull(categoryDto.getChildren());
        assertEquals(0, categoryDto.getChildren().size());

        // 查询不存在的分类
        categoryDto = this.categoryService.getWithChildren(11111L);
        assertNull(categoryDto);
    }

    @Test
    public void listChildrenByPid() {
        assertEquals(4, this.categoryService.listChildrenByPid(1L).size());
        assertEquals(0, this.categoryService.listChildrenByPid(101L).size());
        assertEquals(0, this.categoryService.listChildrenByPid(11111L).size());
    }



}