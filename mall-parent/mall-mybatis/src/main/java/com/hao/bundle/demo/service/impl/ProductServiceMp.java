package com.hao.bundle.demo.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hao.bundle.demo.common.util.MybatisPlusUtil;
import com.hao.bundle.demo.entity.Product;
import com.hao.bundle.demo.mapper.ProductMapper;
import com.hao.bundle.demo.pojo.converter.CategoryConverter;
import com.hao.bundle.demo.pojo.converter.ProductConverter;
import com.hao.bundle.demo.pojo.dto.ProductDto;
import com.hao.bundle.demo.pojo.query.PageQuery;
import com.hao.bundle.demo.pojo.query.ProductQuery;
import com.hao.bundle.demo.pojo.query.SortQuery;
import com.hao.bundle.demo.pojo.query.SortQuery.SortItem;
import com.hao.bundle.demo.pojo.wrapper.PageBean;
import com.hao.bundle.demo.service.IProductService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductServiceMp implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    private final ProductConverter productConverter = ProductConverter.INSTANCE;

    @Override
    public ProductDto get(Long id) {
        Product product = this.productMapper.selectById(id);
        return this.productConverter.entityToDto(product);
    }

    /**
     * 将查询条件转化为 mybatis plus 的 Wrapper
     * @param productQuery 商品查询
     * @param sortQuery 排序字段参数
     * @return 转换后的 wrapper
     */
    private Wrapper<Product> queryToWrapper(ProductQuery productQuery, SortQuery sortQuery) {
        // 将 name 置空，剩余字段采用等值查询
        Product product = productConverter.queryToEntity(productQuery);
        product.setName(null);
        QueryWrapper<Product> wrapper = Wrappers.query(product);

        // name 字段采用模糊查询
        String name = productQuery.getName();
        wrapper.lambda().like(StrUtil.isNotBlank(name), Product::getName, name);

        if (sortQuery != null) {
            List<SortItem> sortItemList = sortQuery.toSortItemList();
            for (SortItem sortItem : sortItemList) {
                // 如果是按名称排序，设置成按中文名排序
                if ("name".equals(sortItem.getProperty())) {
                    wrapper.orderBy(true, sortItem.isAsc(), "CONVERT(name USING gbk)");
                    continue;
                }
                wrapper.orderBy(true, sortItem.isAsc(), sortItem.getProperty());
            }
        }

        return wrapper;
    }

    @Override
    public PageBean<ProductDto> page(ProductQuery productQuery, PageQuery pageQuery, SortQuery sortQuery) {
        Wrapper<Product> wrapper = queryToWrapper(productQuery, sortQuery);

        Page<Product> pageParam = Page.of(pageQuery.getPage(), pageQuery.getSize());
        Page<Product> pageRes = this.productMapper.selectPage(pageParam, wrapper);
        return MybatisPlusUtil.pageConvert(pageRes, ProductDto.class);
    }

    public static void main(String[] args) {
        System.out.println("杰尼龟".compareTo("卡咪龟"));
    }
}
