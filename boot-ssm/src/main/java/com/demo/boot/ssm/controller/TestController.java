package com.demo.boot.ssm.controller;

import com.demo.base.component.entity.Product;
import com.demo.base.component.exception.ResourceNotFoundException;
import com.demo.base.component.pojo.bean.ResBean;
import com.demo.base.component.util.EntityUtil;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
@Api("用于前端测试")
public class TestController {

    private static Map<Long, Product> itemMap = new HashMap<>();


    // 随机初始化 10 个对象
    static {
        for (long i = 1; i <= 10L; i++) {
            itemMap.put(i, EntityUtil.generateRandomOne(Product.class, (int)i));
        }
    }



    // 根据请求参数返回所有 id 比请求 id 小的对象
    @GetMapping("item")
    public List<Product> item_list(@RequestParam(defaultValue = "10000") Long id) {

        List<Product> itemList = Lists.newArrayList();

        for (Map.Entry<Long, Product> entry : itemMap.entrySet()) {
            if (entry.getKey() < id) {
                itemList.add(entry.getValue());
            }
        }

        return itemList;
    }

    // 根据 id 返回一个 item 对象
    @GetMapping("item/{id}")
    public Product item_get(@PathVariable Long id) {

        if (itemMap.containsKey(id)) {
            return itemMap.get(id);
        }

        throw new ResourceNotFoundException("item 不存在");
    }

    // 根据请求新增一个 item，然后返回一个带 id 的 item
    @PostMapping("item")
    public Product item_post(@RequestBody Product item) {
        // 设置 4 位数的 id，其他内容不变
        Long id = RandomUtils.nextLong() % 10000;
        item.setId(id);
        itemMap.put(id, item);
        return item;
    }

    // 只修改 name 和 price
    @PutMapping("item/{id}")
    public ResponseEntity<ResBean> item_put(@PathVariable Long id, @RequestBody Product item) {

        if (itemMap.containsKey(id)) {
            Product realItem = itemMap.get(id);

            if (StringUtils.isNotBlank(item.getName())) {
                realItem.setName(item.getName());
            }

            if (item.getPrice() != null) {
                realItem.setPrice(item.getPrice());
            }

            return ResBean.ok_200("更新成功");
        }

        return ResBean.not_found_404("item 不存在");
    }


    @DeleteMapping("item/{id}")
    public ResponseEntity<ResBean> item_delete(@PathVariable Long id) {

        if (itemMap.containsKey(id)) {
            itemMap.remove(id);
            return ResBean.ok_200("删除成功");
        }

        return ResBean.not_found_404("item 不存在");
    }


    // 返回一个 res 对象，测试 get, post, put, delete 方法

    @GetMapping("res200")
    public ResponseEntity<ResBean> res200() {
        return ResBean.ok_200("成功 success 200");
    }

    @GetMapping("res400")
    public ResponseEntity<ResBean> res400() {
        return ResBean.badRequest_400("参数有误 param error 400");
    }

    @GetMapping("res401")
    public ResponseEntity<ResBean> res401() {
        return ResBean.unauthorized_401("token 失效 unauthorized 401");
    }

    @GetMapping("res403")
    public ResponseEntity<ResBean> res403() {
        return ResBean.forbidden_403("权限不足 no permission 403");
    }

    @GetMapping("res404")
    public ResponseEntity<ResBean> res404() {
        return ResBean.not_found_404("资源不存在 not found 404");
    }

}
