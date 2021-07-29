package com.demo.boot.ssm.service;

import com.demo.base.component.base.BaseService;
import com.demo.base.component.entity.Product;
import com.demo.base.component.mapper2.AddProductMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService extends BaseService<Product> {

    @Autowired
    private AddProductMapper addProductMapper;

    // 复写校验代码，名称相同不允许插入
    @Override
    public String queryUniqueBeforeSave(Product entity) {

        Product build = Product.builder()
                .name(entity.getName()).build();

        // 校验名称是否已存在
        if (this.mapper.selectCount(build) > 0) {
            return String.format("名称为 %s 的记录已存在", entity.getName());
        }

        return null;
    }

    @Override
    public String queryUniqueBeforeUpdate(Product entity) {

        Product item = this.mapper.selectByPrimaryKey(entity.getId());

        boolean change = false;

        if (StringUtils.isNotBlank(entity.getName())
                && !item.getName().equals(entity.getName())) {
            change = true; // 名称发生了变化
        }

        // 校验新名称是否以被使用
        Product build = Product.builder()
                .name(entity.getName())
                .build();
        if (change && this.mapper.selectCount(build) > 0) {
            return "名称为 " + entity.getName() + " 的记录已存在";
        }


        return null;
    }

    public PageInfo<Product> listPageByKeyword(int pageNum, int pageSize, String keyword) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> itemList = this.addProductMapper.searchProductByKeyword(keyword);
        return PageInfo.of(itemList);
    }

}
