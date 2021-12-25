package com.hao.bundle.todo.common.base;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseService<T, ID> {

    @Autowired
    protected BaseDao<T, ID> baseDao;

    public T save(T entity) {
        return baseDao.save(entity);
    }

    public void saveAll(Iterable<T> entities) {
        baseDao.saveAll(entities);
    }

    public T get(ID id) {
        return baseDao.getOne(id);
    }

    public List<T> list() {
        return baseDao.findAll();
    }

}
