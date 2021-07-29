package com.demo.common.mapper;

import com.demo.common.BaseTest;
import com.demo.common.entity.Product;
import com.demo.common.util.EntityUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ItemMapperTest extends BaseTest {

    @Autowired
    private ProductMapper itemMapper;

    @Test
    @Transactional
    @Rollback
    public void insertList() {

        List<Product> itemList = Lists.newArrayList();

        int size = RandomUtils.nextInt(1, 50);

        for (int i = 0; i < size; i++) {
            itemList.add(EntityUtil.generateRandomOne(Product.class, i + 10086));
        }

        int insertCnt = this.itemMapper.insertList(itemList);
        Assert.assertEquals(size, insertCnt);
    }
}