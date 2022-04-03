package com.hao.bundle.demo.service;

import com.hao.bundle.demo.pojo.dto.ProductDto;
import com.hao.bundle.demo.pojo.query.PageQuery;
import com.hao.bundle.demo.pojo.query.ProductQuery;
import com.hao.bundle.demo.pojo.wrapper.Page;

public interface IProductService {

    ProductDto get(Long id);

    /**
     * 分页查询商品
     * @param productQuery 商品查询参数
     * @param pageQuery 分页参数
     * @return 商品分页列表
     */
    Page<ProductDto> page(ProductQuery productQuery, PageQuery pageQuery);

}
