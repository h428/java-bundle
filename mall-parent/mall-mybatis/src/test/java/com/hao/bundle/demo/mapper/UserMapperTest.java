package com.hao.bundle.demo.mapper;

import static org.junit.Assert.*;

import com.hao.bundle.demo.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserMapperTest extends BaseTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void get() {
        System.out.println(this.userMapper.selectById(1L));
        assertNotNull(this.userMapper.selectById(1L));
        assertNotNull(this.userMapper.selectById(2L));
        assertNull(this.userMapper.selectById(1112L));
    }

}