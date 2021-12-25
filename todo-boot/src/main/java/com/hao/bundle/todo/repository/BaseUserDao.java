package com.hao.bundle.todo.repository;

import com.hao.bundle.todo.common.base.BaseDao;
import com.hao.bundle.todo.bean.entity.BaseUserEntity;
import org.springframework.stereotype.Component;

@Component
public interface BaseUserDao extends BaseDao<BaseUserEntity, Long> {

}
