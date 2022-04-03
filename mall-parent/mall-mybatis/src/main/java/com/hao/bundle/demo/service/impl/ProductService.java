package com.hao.bundle.demo.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hao.bundle.demo.common.util.MybatisPlusUtil;
import com.hao.bundle.demo.entity.Product;
import com.hao.bundle.demo.mapper.ProductMapper;
import com.hao.bundle.demo.pojo.converter.ProductConverter;
import com.hao.bundle.demo.pojo.dto.ProductDto;
import com.hao.bundle.demo.pojo.query.PageQuery;
import com.hao.bundle.demo.pojo.query.ProductQuery;
import com.hao.bundle.demo.pojo.wrapper.PageBean;
import com.hao.bundle.demo.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductConverter productConverter;

    @Override
    public ProductDto get(Long id) {
        Product product = this.productMapper.selectById(id);
        return this.productConverter.entityToDto(product);
    }

    /**
     * 将查询条件转化为 mybatis plus 的 Wrapper
     * @param productQuery
     * @return
     */
    private Wrapper<Product> queryToWrapper(ProductQuery productQuery) {
        // 将 name 置空，剩余字段采用等值查询
        Product product = productConverter.queryToEntity(productQuery);
        product.setName(null);
        LambdaQueryWrapper<Product> wrapper = Wrappers.lambdaQuery(product);

        String name = productQuery.getName();
        wrapper.like(StrUtil.isNotBlank(name), Product::getName, name);

        return wrapper;
    }

    @Override
    public PageBean<ProductDto> page(ProductQuery productQuery, PageQuery pageQuery) {
        Wrapper<Product> wrapper = queryToWrapper(productQuery);

        Page<Product> pageParam = Page.of(pageQuery.getPage(), pageQuery.getSize());
        Page<Product> pageRes = this.productMapper.selectPage(pageParam, wrapper);
        return MybatisPlusUtil.pageConvert(pageRes, ProductDto.class);
    }
}
