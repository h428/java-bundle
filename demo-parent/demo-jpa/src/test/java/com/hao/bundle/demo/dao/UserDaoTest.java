package com.hao.bundle.demo.dao;

import com.hao.bundle.demo.BaseTest;
import com.hao.bundle.demo.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

public class UserDaoTest extends BaseTest {

    // findById 方法会立即（EAGER）访问数据库，并返回和指定 ID 关联的实体对象；如果没有找到，则返回 Optional.empty()
    // getOne 是一个延迟加载方法，它并不立即访问数据库，而是返回一个代理（proxy）对象，这个代理对象是对实体对象的引用，仅在使用代理对象访问对象属性时才会去真正访问数据库 ，如果找不到，则抛出 EntityNotFoundException
    // findOne 方法：多条件查询，立即访问数据库，如果不存在返回 Optional.empty()

    @Autowired
    private UserDao userDao;

    @Test
    public void test() {
        System.out.println(this.userDao.findById(1L).isPresent());
        System.out.println(this.userDao.findById(111L).isPresent());
        System.out.println(this.userDao.findOne(Example.of(User.builder().id(1L).build())));
        System.out.println(this.userDao.findOne(Example.of(User.builder().id(111L).build())));

    }

}