package com.demo.common.service;

import com.demo.common.base.BaseService;
import com.demo.common.entity.Product;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;

import java.util.Set;

// 该 Service 用于 BaseService 的测试
@Service
public class ItemService extends BaseService<Product> {

    // 允许根据 id, name 进行排序
    @Override
    protected Set<String> initOrderBySet() {
        return Sets.newHashSet("id", "name");
    }
}
