package com.demo.common.pojo.converter;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.ParameterizedType;

public abstract class PojoConverter<POJO, ENTITY> {

    private Class<ENTITY> entityClass;

    public PojoConverter() {
        // 0 是 DTO, 1 是 ENTITY
        this.entityClass = (Class<ENTITY>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    public ENTITY convertToEntity() {
        try {
            ENTITY b = this.entityClass.newInstance();
            BeanUtils.copyProperties(this, b);
            return b;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public POJO convertFromEntity(ENTITY b) {
        try {
            BeanUtils.copyProperties(b, this);
            return (POJO) this;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
