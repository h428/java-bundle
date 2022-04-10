package com.hao.bundle.demo.dao;

import com.hao.bundle.demo.common.base.BaseDao;
import com.hao.bundle.demo.entity.Category;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryDao extends BaseDao<Category, Long> {

    /**
     * 根据 pid 查询子分类列表
     * @param pid 父分类 id
     * @return 子分类列表
     */
    List<Category> findByPid(Long pid);

    /**
     * 即 existChildrenByPid，但 jpa 不允许随意命名；
     * 根据 pid 查询是否存在子分类
     * @param pid 父分类 id
     * @return 存在则 true，否则 false
     */
    boolean existsByPid(Long pid);

}
