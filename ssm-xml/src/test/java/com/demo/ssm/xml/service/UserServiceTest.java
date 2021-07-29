package com.demo.ssm.xml.service;

import com.demo.ssm.xml.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class UserServiceTest extends BaseTest {

    @Autowired
    private UserService userService;


    @Test
    public void test() {
        System.out.println(this.userService.getById(1L));
    }

    @Test
    public void test2() {
        System.out.println(this.userService.listAll());
    }


}