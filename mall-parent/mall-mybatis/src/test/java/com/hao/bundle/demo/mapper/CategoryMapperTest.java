package com.hao.bundle.demo.mapper;

import static org.junit.Assert.*;

import com.hao.bundle.demo.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryMapperTest extends BaseTest {

    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    public void existById() {
        // id 对应的记录存在的情况
        assertTrue(categoryMapper.existById(1L));

        // id 对应的记录不存在的情况
        assertFalse(categoryMapper.existById(11111L));
    }

    @Test
    public void existChildrenByPid() {
        // 存在父分类 pid 为指定 id 的情况
        assertTrue(categoryMapper.existChildrenByPid(1L));

        // pid 记录自身存在，但不存在其他子分类的 pid 为指定 pid 的情况
        assertFalse(categoryMapper.existChildrenByPid(104L));

        // pid 记录自身不存在，且更无其他子分类的 pid 为指定 pid 的情况
        assertFalse(categoryMapper.existChildrenByPid(1111111111L));
    }
}