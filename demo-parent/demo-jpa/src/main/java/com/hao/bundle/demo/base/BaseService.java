package com.hao.bundle.demo.base;

import com.hao.bundle.demo.common.util.ClassUtil;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.transaction.annotation.Transactional;

/**
 * 通用 Service 实现，实体带 deleted 属性用于伪删除
 */
@Transactional
public abstract class BaseService<T, ID> {

    private static final String DELETED = "deleted";
    private static final String ID = "id";
    private static final String DEL_TIME = "delTime";
    private static final String NAME = "name";
    private static final String DESC = "desc";
    private static final String ASC = "asc";

    @Autowired
    private BaseDao<T, ID> baseDao;

    /**
     * 实体类型 T.class
     */
    protected Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public BaseService() {
        // 获取父类类型并转化为参数化类型 ParameterizedType 以获取泛型信息
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        // 使用参数化类型获取泛型的实际信息
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    public T getById(ID id) {
        return this.baseDao.getOne(id);
    }

    public T getById(ID id, Boolean deleted) {
        T entity = ClassUtil.newInstance(entityClass);
        ClassUtil.setValue(entity, ID, id);
        ClassUtil.setValue(entity, DELETED, deleted);
        Example<T> example = Example.of(entity);

        Optional<T> optional = this.baseDao.findOne(example);

        return optional.orElse(null);

    }


    public List<T> listByEntity(T entity) {
        Example<T> example = Example.of(entity);
        return this.baseDao.findAll(example);
    }

    // ...


}
