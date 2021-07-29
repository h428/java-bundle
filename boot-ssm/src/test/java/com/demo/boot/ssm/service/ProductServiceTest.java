package com.demo.boot.ssm.service;

import static org.junit.Assert.assertEquals;

import com.demo.base.component.entity.Product;
import com.demo.boot.ssm.BaseTest;
import com.google.common.collect.Lists;
import java.util.List;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


public class ProductServiceTest extends BaseTest {

    @Autowired
    private ProductService productService;

    @Test
    public void saveSelective() {
    }

    @Transactional
    @Rollback(false)
    @Test
    public void saveList() {

        List<Product> itemList = Lists.newArrayList();

        int cnt = 70000;

        for (long idx = 0; idx < cnt; ++idx) {

            long id = idx + 100;
            long cid1 = id % 4 + 1;
            long cid2;

            if (cid1 == 1) {
                cid2 = RandomUtils.nextInt() % 4 + 101L;
            } else if (cid1 == 2) {
                cid2 = RandomUtils.nextInt() % 6 + 201L;
            } else if (cid1 == 3) {
                cid2 = RandomUtils.nextInt() % 5 + 301L;
            } else {
                cid2 = RandomUtils.nextInt() % 4 + 401L;
            }

            Product item = Product.builder()
                    .id(id)
                    .name("item" + id)
                    .price(id + 0.1f)
                    .description("")
                    .detail("")
                    .images("")
                    .note("")
                    .status(0)
                    .cid1(cid1)
                    .cid2(cid2)
                    .deleted(id % 10 == 0)
                    .delTime(id % 10 == 0 ? System.currentTimeMillis() : 0L)
                    .build();
            itemList.add(item);
        }

        assertEquals(cnt, this.productService.saveList(itemList));

    }


}