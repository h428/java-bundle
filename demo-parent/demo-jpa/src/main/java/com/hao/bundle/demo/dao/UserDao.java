package com.hao.bundle.demo.dao;

import com.hao.bundle.demo.common.base.BaseDao;
import com.hao.bundle.demo.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseDao<User, Long> {

    // findById 方法会立即（EAGER）访问数据库，并返回和指定 ID 关联的实体对象；如果没有找到，则返回 Optional.empty()
    // getOne 是一个延迟加载方法，它并不立即访问数据库，而是返回一个代理（proxy）对象，这个代理对象是对实体对象的引用，仅在 使用代理对象访问对象属性时才会去真正访问数据库 ，如果找不到，则抛出 EntityNotFoundException
    // findOne 方法：多条件查询，立即访问数据库

    User getByEmail(String email);

    User getByUserName(String userName);


}
