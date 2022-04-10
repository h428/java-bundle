package com.hao.bundle.demo.dao;

import static org.junit.Assert.*;
import com.hao.bundle.demo.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserDaoTest extends BaseTest {

    // findById 方法会立即（EAGER）访问数据库，并返回和指定 ID 关联的实体对象；如果没有找到，则返回 Optional.empty()
    // getOne 是一个延迟加载方法，它并不立即访问数据库，而是返回一个代理（proxy）对象，这个代理对象是对实体对象的引用，仅在使用代理对象访问对象属性时才会去真正访问数据库 ，如果找不到，则抛出 EntityNotFoundException
    // findOne 方法：多条件查询，立即访问数据库，如果不存在返回 Optional.empty()

    @Autowired
    private UserDao userDao;


    @Test
    public void getByEmail() {
        assertEquals("cat", this.userDao.getByEmail("cat@hao.com").getUserName());
        assertEquals("dog", this.userDao.getByEmail("dog@hao.com").getUserName());
        assertNull(this.userDao.getByEmail("dog222@hao.com"));
    }

    @Test
    public void getByUserName() {
        assertEquals("cat", this.userDao.getByUserName("cat").getUserName());
        assertNull(this.userDao.getByUserName("cat222"));
    }
}