package com.hao.bundle.todo.service;

import static org.junit.Assert.*;

import com.hao.bundle.todo.BaseTest;
import com.hao.bundle.todo.bean.dto.BaseUserDTO;
import com.hao.bundle.todo.common.util.EntityUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseUserServiceTest extends BaseTest {

    @Autowired
    private BaseUserService baseUserService;

    @Test
    public void save() {

    }

    @Test
    public void update() {
    }

    @Test
    public void get() {
        BaseUserDTO baseUserDTO = baseUserService.get(111L);
        EntityUtil.printString(baseUserDTO);
    }


    @Test
    public void loginByEmail() {
        BaseUserDTO baseUserDTO = baseUserService.loginByEmail("lyh56231947702657301870@126.com",
            "");
        System.out.println(baseUserDTO);
    }
}