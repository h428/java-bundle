package com.demo.common.mapper2;

import com.demo.common.BaseTest;
import com.demo.common.entity.Product;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AddItemMapperTest extends BaseTest {


    @Autowired
    private AddProductMapper addItemMapper;

    @Test
    public void searchItemByKeyword() {

        // 搜索"妙蛙"
        List<Product> itemList = this.addItemMapper.searchProductByKeyword("妙蛙");

        assertNotNull(itemList);

        assertEquals(3, itemList.size()); // 结果为 3 个

        // 搜索顺序好像不敢保证
    }
}