package com.hao.bundle.demo.dao;

import static org.junit.Assert.*;

import com.hao.bundle.demo.BaseTest;
import com.hao.bundle.demo.entity.User;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserDaoTest extends BaseTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void test() {
        List<User> all = this.userDao.findAll();
        System.out.println(all);
    }

}