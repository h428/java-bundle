package com.hao.bundle.demo.dao;

import com.hao.bundle.demo.common.base.BaseDao;
import com.hao.bundle.demo.entity.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDao extends BaseDao<Product, Long> {


}
