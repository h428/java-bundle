package com.hao.bundle.demo.service.impl;

import com.hao.bundle.demo.entity.Product;
import com.hao.bundle.demo.mapper.ProductMapper;
import com.hao.bundle.demo.pojo.converter.ProductConverter;
import com.hao.bundle.demo.pojo.dto.ProductDto;
import com.hao.bundle.demo.pojo.query.PageQuery;
import com.hao.bundle.demo.pojo.query.ProductQuery;
import com.hao.bundle.demo.pojo.wrapper.Page;
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

    @Override
    public Page<ProductDto> page(ProductQuery productQuery, PageQuery pageQuery) {
        return null;
    }
}
