package com.demo.common.base;

import com.demo.common.BaseTest;
import com.demo.common.entity.Product;
import com.demo.common.pojo.dto.ProductDTO;
import com.demo.common.service.ItemService;
import com.demo.common.util.EntityUtil;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

public class BaseServiceTest extends BaseTest {


    // 以 ItemService 为样本测试 BaseService 基本方法

    @Autowired
    private ItemService itemService;

    private static final float delta = 1e-6f;

    // Item 的通用判空
    private void assertItemNotNull(Product item) {
        assertNotNull(item);
        assertNotNull(item.getId());
        assertNotNull(item.getName());
        assertNotNull(item.getPrice());
        assertNotNull(item.getDescription());
        assertNotNull(item.getDetail());
        assertNotNull(item.getImages());
        assertNotNull(item.getNote());
        assertNotNull(item.getStatus());
        assertNotNull(item.getCid1());
        assertNotNull(item.getCid2());
        assertNotNull(item.getDeleted());
        assertNotNull(item.getDelTime());
    }


    // 判断前三个 item : 妙蛙种子, 妙蛙草, 妙蛙花
    private void assertFirstThreeItems(List<Product> itemList) {
        // 判断 item=1 的名称，cid1, cid2
        assertEquals("妙蛙种子", itemList.get(0).getName());
        assertEquals(318.0f, itemList.get(0).getPrice(), delta);
        assertEquals(1L, itemList.get(0).getCid1().longValue());
        assertEquals(101L, itemList.get(0).getCid2().longValue());

        // 判断 item=2 的名称，cid1, cid2
        assertEquals("妙蛙草", itemList.get(1).getName());
        assertEquals(405.0f, itemList.get(1).getPrice(), delta);
        assertEquals(1L, itemList.get(1).getCid1().longValue());
        assertEquals(101L, itemList.get(1).getCid2().longValue());

        // 判断 item=3 的名称，cid1, cid2
        assertEquals("妙蛙花", itemList.get(2).getName());
        assertEquals(525.0f, itemList.get(2).getPrice(), delta);
        assertEquals(1L, itemList.get(2).getCid1().longValue());
        assertEquals(101L, itemList.get(2).getCid2().longValue());
    }

    // 校验 pageSize = 3, cid1=1 的第 2 页
    private void assertPage2WithSize3(PageInfo<Product> pageInfo) {
        // 校验总数，页数
        assertEquals(9L, pageInfo.getTotal());
        assertEquals(3, pageInfo.getPages());

        // 校验第 2 页数据长度为 3
        List<Product> itemList = pageInfo.getList();
        assertNotNull(itemList);
        assertEquals(3, itemList.size());

        // 校验第 2 页数据 : cid1 = 1, cid2 = 102
        itemList.forEach(item -> {
            assertEquals(1L, item.getCid1().longValue());
            assertEquals(102L, item.getCid2().longValue());
        });

        // 第二页的第最后一个 item : 喷火龙
        assertEquals("喷火龙", itemList.get(2).getName());
    }


    @Test
    public void getById() {
        Product item = this.itemService.getById(1L);
        // 通用判空
        assertItemNotNull(item);
        // 判断 item=1 的名称，cid1, cid2
        assertEquals("妙蛙种子", item.getName());
        assertEquals(318.0f, item.getPrice(), delta);
        assertEquals(1L, item.getCid1().longValue());
        assertEquals(101L, item.getCid2().longValue());

    }



    @Test
    public void listByIds() {
        List<Product> itemList = this.itemService.listByIds(Lists.newArrayList(1L, 2L, 3L));
        // 校验包含 3 个 item
        assertNotNull(itemList);
        assertEquals(3, itemList.size());
        // 逐一对每个 item 进行判空
        itemList.forEach(this::assertItemNotNull);
        // 校验前 3 个 item
        assertFirstThreeItems(itemList);
    }

    @Test
    public void listAll() {
        List<Product> itemList = this.itemService.listAll();
        // 校验总共包含 15 个 item
        assertNotNull(itemList);
        assertEquals(15, itemList.size());
        // 校验前 3 个 item
        assertFirstThreeItems(itemList);
    }

    @Test
    public void getOneByEntity() {
        // 查询名为为妙蛙草的那条记录
        Product item = Product.builder().name("妙蛙草").build();
        item = this.itemService.getOneByEntity(item);
        // 通用判空
        assertItemNotNull(item);
        // 判断 item=2 的名称，cid1, cid2
        assertEquals("妙蛙草", item.getName());
        assertEquals(405.0f, item.getPrice(), delta);
        assertEquals(1L, item.getCid1().longValue());
        assertEquals(101L, item.getCid2().longValue());
        try {
            // 必定异常
            item = Product.builder().cid2(101L).build();
            item = this.itemService.getOneByEntity(item);
            assertNull(item);
        } catch (MyBatisSystemException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void countByEntity() {
        // 验证 cid1 = 1 的记录条数为 9 条
        assertEquals(9, this.itemService.countByEntity(Product.builder().cid1(1L).build()));
        // 验证 cid1 = 3 的记录条数为 6 条
        assertEquals(6, this.itemService.countByEntity(Product.builder().cid1(3L).build()));
        // 验证 cid2 = 101 的记录条数为 3 条
        assertEquals(3, this.itemService.countByEntity(Product.builder().cid2(101L).build()));
        // 验证 cid2 = 302 的记录条数为 6 条
        assertEquals(6, this.itemService.countByEntity(Product.builder().cid2(302L).build()));
        // 验证 cid1 不存在的记录为 0 条
        assertEquals(0, this.itemService.countByEntity(Product.builder().cid1(0L).build()));
        // 验证 cid2 不存在的记录为 0 条
        assertEquals(0, this.itemService.countByEntity(Product.builder().cid2(0L).build()));
    }

    @Test
    public void listObjectsByEntity() {

        // 验证所有 cid1 = 1 的 item
        List<Product> itemList = this.itemService.listObjectsByEntity(Product.builder().cid1(1L).build());

        // 校验 cid1=1 共有 9 个 item
        assertNotNull(itemList);
        assertEquals(9, itemList.size());

        // 对每个 item 逐一通用判空
        itemList.forEach(this::assertItemNotNull);

        // 对每个 item 指定特定判断
        itemList.forEach(item -> {
            // cid1 = 1
            assertEquals(1L, item.getCid1().longValue());
            // cid2 = 101 或 102 或 103
            assertTrue(item.getCid2().equals(101L)
                    || item.getCid2().equals(102L)
                    || item.getCid2().equals(103L));
        });

        // 验证 cid1 = 3 共有 6 个 item
        assertEquals(6, this.itemService.listObjectsByEntity(Product.builder().cid1(3L).build()).size());

        // 验证 cid2 = 302 共有 6 个 item
        assertEquals(6, this.itemService.listObjectsByEntity(Product.builder().cid2(302L).build()).size());

        // 验证不存在的 cid1 没有 item，注意 list 不为 null 但 size = 0
        assertEquals(0, this.itemService.listObjectsByEntity(Product.builder().cid1(10086L).build()).size());

        // 验证不存在的 cid2 没有 item
        assertEquals(0, this.itemService.listObjectsByEntity(Product.builder().cid2(10010L).build()).size());
    }

    @Test
    public void listPageByEntity() {

        // 按 pageSize = 3 查出 cid1=1 的第 2 页
        PageInfo<Product> pageInfo = this.itemService.listPageByEntity(2, 3, Product.builder().cid1(1L).build(), "id", false);

        assertPage2WithSize3(pageInfo);
    }

    @Test
    public void listPageByQueryDto() {

        // 用 ItemDTO 做 QueryDTO
        PageInfo<Product> pageInfo = this.itemService.listPageByQueryDTO(2, 3, ProductDTO.builder().cid1(1L).build(), "id", false);

        assertPage2WithSize3(pageInfo);
    }

    @Test
    public void listPageByName() {


        PageInfo<Product> pageInfo = this.itemService.listPageByName(1, 3, "妙蛙", "id", false);

        // 校验总数，页数
        assertEquals(3L, pageInfo.getTotal());
        assertEquals(1, pageInfo.getPages());


        // 校验第 1 页数据长度为 3
        List<Product> itemList = pageInfo.getList();
        assertNotNull(itemList);
        assertEquals(3, itemList.size());

        // 校验第 1 页数据 : cid1 = 1, cid2 = 101
        itemList.forEach(item -> {
            assertEquals(1L, item.getCid1().longValue());
            assertEquals(101L, item.getCid2().longValue());
        });

        // 第二页的第最后一个 item : 妙蛙花
        assertEquals("妙蛙花", itemList.get(2).getName());

    }

    // no service
    @Test
    public void queryUniqueBeforeSave() {
        assertNull(this.itemService.queryUniqueBeforeSave(null));

    }

    // no service
    @Test
    public void queryUniqueBeforeUpdate() {
        assertNull(this.itemService.queryUniqueBeforeUpdate(null));
    }

    @Test
    public void queryCheckBeforeDelete() {
        assertNull(this.itemService.queryCheckBeforeDelete(null));
    }

    // no parser, no service
    @Test
    public void saveParseResult() {

    }

    @Transactional
    @Rollback // 回滚事务，不污染数据
    @Test
    public void saveSelective() {
        // 生成 id = 22, 23, 24 的三个 item 对象并保存
        assertEquals(1, this.itemService.saveSelective(EntityUtil.generateRandomOne(Product.class, 22)));
        assertEquals(1, this.itemService.saveSelective(EntityUtil.generateRandomOne(Product.class, 23)));
        assertEquals(1, this.itemService.saveSelective(EntityUtil.generateRandomOne(Product.class, 27)));
    }

    @Transactional
    @Rollback // 回滚事务，不污染数据
    @Test
    public void saveList() {

        List<Product> itemList = Lists.newArrayList();

        // 生成 5 个 item
        for (int i = 0; i < 5; ++i) {
            Product item = EntityUtil.generateRandomOne(Product.class, 50 + i);
            itemList.add(item);
        }

        // 插入 5 个 item
        assertEquals(itemList.size(), this.itemService.saveList(itemList));
    }

    @Transactional
    @Rollback // 回滚事务，不污染数据
    @Test
    public void updateSelectiveById() {
        // 生成 id = 1, 2, 3 的三个 item 对象并 update
        assertEquals(1, this.itemService.updateSelectiveById(EntityUtil.generateRandomOne(Product.class, 1)));
        assertEquals(1, this.itemService.updateSelectiveById(EntityUtil.generateRandomOne(Product.class, 2)));
        assertEquals(1, this.itemService.updateSelectiveById(EntityUtil.generateRandomOne(Product.class, 3)));

        // 更新 id 不存在的记录
        assertEquals(0, this.itemService.updateSelectiveById(EntityUtil.generateRandomOne(Product.class, 10086)));
        assertEquals(0, this.itemService.updateSelectiveById(EntityUtil.generateRandomOne(Product.class, 10010)));
    }

    @Transactional
    @Rollback // 回滚事务，不污染数据
    @Test
    public void deleteById() {
        // 删除 id=1,3,5 三个数据
        assertEquals(1, this.itemService.deleteById(1L));
        assertEquals(1, this.itemService.deleteById(3L));
        assertEquals(1, this.itemService.deleteById(5L));


        // 删除记录不存在的数据
        assertEquals(0, this.itemService.deleteById(10086L));
        assertEquals(0, this.itemService.deleteById(10010L));
    }

    @Transactional
    @Rollback // 回滚事务，不污染数据
    @Test
    public void deleteByIds() {
        // 删除 id= 1, 3, 5, 7 共 4 个数据
        assertEquals(4, this.itemService.deleteByIds("id", Lists.newArrayList(1, 3, 5,7)));

        // 删除 4 个 id 不存在记录，返回 0
        assertEquals(0, this.itemService.deleteByIds("id", Lists.newArrayList(10086, 10010, 10000, 12580)));

        // 删除两个 id 存在，两个 id 不存在的记录，返回 2（注意不要和上面的删除过的 id 重合）
        assertEquals(2, this.itemService.deleteByIds("id", Lists.newArrayList(2, 10086, 4, 10010)));
    }

    @Transactional
    @Rollback // 回滚事务，不污染数据
    @Test
    public void deleteByEntity() {

        // 删除 cid1 = 3 的记录，共 6 条
        assertEquals(6, this.itemService.deleteByEntity(Product.builder().cid1(3L).build()));

        // 删除 cid2 = 102 的记录， 共 3 条
        assertEquals(3, this.itemService.deleteByEntity(Product.builder().cid2(102L).build()));

        // 再在上一条的基础上删除 cid1 = 1 的记录，本来共 9 条，由于上一条删除了 3 条，故剩下共 6 条
        assertEquals(6, this.itemService.deleteByEntity(Product.builder().cid1(1L).build()));
    }

    @Transactional
    @Rollback // 回滚事务，不污染数据
    @Test
    public void fakeDeleteById() {
        assertEquals(1, this.itemService.fakeDeleteById(1L));
        assertEquals(1, this.itemService.fakeDeleteById(2L));
        assertEquals(1, this.itemService.fakeDeleteById(3L));
    }


    @Test
    public void testRun() {

        Product item = Product.builder().cid1(3L).build();

        PageInfo<Product> pageInfo = this.itemService.listPageByEntity(1, 5, item, "name", false);

        EntityUtil.printString(pageInfo.getList());
    }

}