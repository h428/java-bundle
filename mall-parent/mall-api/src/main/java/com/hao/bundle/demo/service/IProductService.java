package com.hao.bundle.demo.service;

import com.hao.bundle.demo.pojo.dto.ProductDto;
import com.hao.bundle.demo.pojo.query.PageQuery;
import com.hao.bundle.demo.pojo.query.ProductQuery;
import com.hao.bundle.demo.pojo.query.SortQuery;
import com.hao.bundle.demo.pojo.wrapper.PageBean;

public interface IProductService {

    /**
     * 根据 id 查询单个商品
     * @param id 商品 id
     * @return 商品信息
     */
    ProductDto get(Long id);

    /**
     * 分页查询商品
     * @param productQuery 商品查询参数
     * @param pageQuery 分页参数
     * @param sortQuery 排序参数
     * @return 商品分页列表
     */
    PageBean<ProductDto> page(ProductQuery productQuery, PageQuery pageQuery, SortQuery sortQuery);

}
