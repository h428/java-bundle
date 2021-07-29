package com.demo.common.base;

import com.demo.common.BaseTest;
import com.demo.common.entity.Product;
import com.demo.common.pojo.dto.ProductDTO;
import com.demo.common.service.ItemServiceWithDelete;
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

public class BaseServiceWithDeletedTest extends BaseTest {

    @Autowired
    private ItemServiceWithDelete itemService;

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

        Product item = this.itemService.getById(1L, false);

        // 通用判空
        assertItemNotNull(item);

        // 判断 item=1 的名称，cid1, cid2
        assertEquals("妙蛙种子", item.getName());
        assertEquals(318.0f, item.getPrice(), delta);
        assertEquals(1L, item.getCid1().longValue());
        assertEquals(101L, item.getCid2().longValue());

        // 校验 id 存在但已经标记的 deleted 无法取出
        assertNull(this.itemService.getById(10L, false));
        assertNull(this.itemService.getById(10L, false));
        assertNull(this.itemService.getById(10L, false));
    }

    @Test
    public void listByIds() {

        List<Product> itemList = this.itemService.listByIds(Lists.newArrayList(1L, 2L, 3L, 10L, 11L), false);

        // 校验包含 3 个 item, id = 10, 11, 12 不会取出
        assertNotNull(itemList);
        assertEquals(3, itemList.size());

        // 逐一对每个 item 进行判空
        itemList.forEach(this::assertItemNotNull);

        // 校验前 3 个 item
        assertFirstThreeItems(itemList);
    }

    @Test
    public void listAll() {

        List<Product> itemList = this.itemService.listAll(false);

        // 校验总共包含 12 个 item，本来共 15 条记录，但有 3 条标记为 deleted
        assertNotNull(itemList);
        assertEquals(12, itemList.size());

        // 校验前 3 个 item
        assertFirstThreeItems(itemList);

    }

    @Test
    public void getOneByEntity() {
        // 查询名为为妙蛙草的那条记录
        Product item = Product.builder().name("妙蛙草").build();
        item = this.itemService.getOneByEntity(item, false);

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
            item = this.itemService.getOneByEntity(item, false);
            assertNull(item); // 必定不执行
        } catch (MyBatisSystemException e) {
            assertNotNull(e);
        }


        // 校验标记为 deleted 的记录无法取出
        assertNull(this.itemService.getOneByEntity(Product.builder().id(10L).build(), false));
        assertNull(this.itemService.getOneByEntity(Product.builder().id(11L).build(), false));
        assertNull(this.itemService.getOneByEntity(Product.builder().id(12L).build(), false));

        // 没有标记为 delete 的记录可以取出
        assertNotNull(this.itemService.getOneByEntity(Product.builder().id(13L).build(), false));
        assertNotNull(this.itemService.getOneByEntity(Product.builder().id(14L).build(), false));
        assertNotNull(this.itemService.getOneByEntity(Product.builder().id(15L).build(), false));
    }

    @Test
    public void countByEntity() {

        // 验证 cid1 = 1 的记录条数为 9 条
        assertEquals(9, this.itemService.countByEntity(Product.builder().cid1(1L).build(), false));

        // 验证 cid1 = 3 的记录条数为 3 条 : 本来有 6 条，但其中有三条标记为 deleted
        assertEquals(3, this.itemService.countByEntity(Product.builder().cid1(3L).build(), false));

        // 验证 cid2 = 101 的记录条数为 3 条
        assertEquals(3, this.itemService.countByEntity(Product.builder().cid2(101L).build(), false));

        // 验证 cid2 = 302 的记录条数为 3 条 : 本来有 6 条，但其中有三条标记为 deleted
        assertEquals(3, this.itemService.countByEntity(Product.builder().cid2(302L).build(), false));

        // 验证 cid1 不存在的记录为 0 条
        assertEquals(0, this.itemService.countByEntity(Product.builder().cid1(0L).build(), false));

        // 验证 cid2 不存在的记录为 0 条
        assertEquals(0, this.itemService.countByEntity(Product.builder().cid2(0L).build(), false));
    }

    @Test
    public void listObjectsByEntity() {

        // 验证所有 cid1 = 1 的 item
        List<Product> itemList = this.itemService.listObjectsByEntity(Product.builder().cid1(1L).build(), false);

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

        // 验证 cid1 = 3 共有 3 个 item，原来有 6 个，但 3 个标记为 deleted
        assertEquals(3, this.itemService.listObjectsByEntity(Product.builder().cid1(3L).build(), false).size());

        // 验证 cid2 = 302 共有 3 个 item，原来有 6 个，但 3 个标记为 deleted
        assertEquals(3, this.itemService.listObjectsByEntity(Product.builder().cid2(302L).build(), false).size());

        // 验证不存在的 cid1 没有 item，注意 list 不为 null 但 size = 0
        assertEquals(0, this.itemService.listObjectsByEntity(Product.builder().cid1(10086L).build(), false).size());

        // 验证不存在的 cid2 没有 item
        assertEquals(0, this.itemService.listObjectsByEntity(Product.builder().cid2(10010L).build(), false).size());
    }

    @Test
    public void listPageByEntity() {

        // 按 pageSize = 3 查出 cid1=1 的第 2 页
        PageInfo<Product> pageInfo = this.itemService.listPageByEntity(2, 3, Product.builder().cid1(1L).build(), "id", false, false);

        assertPage2WithSize3(pageInfo);

        // 查出 cid1=3 共 3 条记录，pageSize = 2 时，第 2 页只有一条数据（原本共 6 条记录，但三条标记为 deleted）
        pageInfo = this.itemService.listPageByEntity(2, 2, Product.builder().cid1(3L).build(), "id", false, false);

        // 校验 cid1=3 共三条
        assertEquals(3L, pageInfo.getTotal());
        // 校验第 2 页只有 1 条数据
        assertEquals(1, pageInfo.getList().size());
    }

    @Test
    public void listPageByQueryDTO() {

        // 用 ItemDTO 做 QueryDTO
        PageInfo<Product> pageInfo = this.itemService.listPageByQueryDTO(2, 3, ProductDTO.builder().cid1(1L).build(), "id", false, false);

        assertPage2WithSize3(pageInfo);

        // 查出 cid1=3 共 3 条记录，pageSize = 2 时，第 2 页只有一条数据（原本共 6 条记录，但三条标记为 deleted）
        pageInfo = this.itemService.listPageByQueryDTO(2, 2, ProductDTO.builder().cid1(3L).build(), "id", false, false);

        // 校验 cid1=3 共三条
        assertEquals(3L, pageInfo.getTotal());
        // 校验第 2 页只有 1 条数据
        assertEquals(1, pageInfo.getList().size());

    }

    @Test
    public void listPageByName() {

        PageInfo<Product> pageInfo = this.itemService.listPageByName(1, 3, "妙蛙", "id", false, false);

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


        // 校验根据关键字“虫”查找只有一个结果，本来有两个，但绿毛虫被标记为 deleted，只剩下一个独角虫
        pageInfo = this.itemService.listPageByName(1, 5, "虫", "id", false, false);

        // 只有一条记录
        assertEquals(1L, pageInfo.getTotal());
        assertEquals(1L, pageInfo.getList().size());

        // 取出该记录做校验 : 是独角虫，cid1=3, cid2=302
        assertEquals("独角虫", pageInfo.getList().get(0).getName());
        assertEquals(3L, pageInfo.getList().get(0).getCid1().longValue());
        assertEquals(302L, pageInfo.getList().get(0).getCid2().longValue());
    }

    @Test
    public void queryUniqueBeforeSave() {
        // 空实现，返回 null
        assertNull(this.itemService.queryUniqueBeforeSave(null));
    }

    @Test
    public void queryUniqueBeforeUpdate() {
        // 空实现，返回 null
        assertNull(this.itemService.queryUniqueBeforeUpdate(null));
    }

    @Test
    public void queryCheckBeforeDelete() {
        // 空实现，返回 null
        assertNull(this.itemService.queryCheckBeforeDelete(null));
    }

    // no parser, no test
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

    @Transactional
    @Rollback // 回滚事务，不污染数据
    @Test
    public void fakeDeleteByIds() {
        // 注意原来已经标记为 deleted 的可以重新删除，毕竟只是做 update，而做 update 之前是没有校验是否已经标记为 deleted 了
        // 因此下面这个测试返回 6 而不是 3
        assertEquals(6, this.itemService.fakeDeleteByIds(Lists.newArrayList(1L, 2L, 3L, 10L, 11L, 12L)));
    }

    @Transactional
    @Rollback // 回滚事务，不污染数据
    @Test
    public void fakeDeleteByEntity() {
        // 删除 cid1 = 3 的记录，共 6 条
        assertEquals(6, this.itemService.fakeDeleteByEntity(Product.builder().cid1(3L).build()));

        // 删除 cid2 = 102 的记录， 共 3 条
        assertEquals(3, this.itemService.fakeDeleteByEntity(Product.builder().cid2(102L).build()));

        // 再在上一条的基础上删除 cid1 = 1 的记录，
        // 但由于本质上是 update，因此虽然本来在上一行删除了三条，此次仍然可以更新 9，故共 9 条，这和真正的 deleted 不一样
        assertEquals(9, this.itemService.fakeDeleteByEntity(Product.builder().cid1(1L).build()));
    }
}