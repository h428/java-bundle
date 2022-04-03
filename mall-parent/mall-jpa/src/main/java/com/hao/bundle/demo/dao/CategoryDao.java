package com.hao.bundle.demo.dao;

import com.hao.bundle.demo.common.base.BaseDao;
import com.hao.bundle.demo.entity.Category;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryDao extends BaseDao<Category, Long> {

    List<Category> findByPid(Long pid);

}
