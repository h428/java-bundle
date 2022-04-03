package com.hao.bundle.demo.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.hao.bundle.demo.common.util.JpaUtil;
import com.hao.bundle.demo.dao.ProductDao;
import com.hao.bundle.demo.entity.Product;
import com.hao.bundle.demo.pojo.converter.ProductConverter;
import com.hao.bundle.demo.pojo.dto.ProductDto;
import com.hao.bundle.demo.pojo.query.PageQuery;
import com.hao.bundle.demo.pojo.query.ProductQuery;
import com.hao.bundle.demo.pojo.wrapper.PageBean;
import com.hao.bundle.demo.service.IProductService;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService implements IProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductConverter productConverter;

    @Override
    public ProductDto get(Long id) {
        Product product = this.productDao.getOne(id);
        return this.productConverter.entityToDto(product);
    }

    @Override
    public PageBean<ProductDto> page(ProductQuery productQuery, PageQuery pageQuery) {
        return this.pageBySpecification(productQuery, pageQuery);
    }

    private PageBean<ProductDto> pageBySpecification(ProductQuery productQuery, PageQuery pageQuery) {

        PageRequest pageRequest = PageRequest.of(pageQuery.getPage(), pageQuery.getSize());

        Specification<Product> specification = (root, query, cb) -> {

            List<Predicate> predicateList = new ArrayList<>();
            
            // 根据名称按 like 模糊查询
            String name = productQuery.getName();
            if (StrUtil.isNotEmpty(name)) {
                Predicate p = cb.like(root.get("name"), "%" + name + "%");
                predicateList.add(p);
            }

            // 根据 cid1 等值查询
            Long cid1 = productQuery.getCid1();
            if (cid1 != null) {
                Predicate p = cb.equal(root.get("cid1"), cid1);
                predicateList.add(p);
            }

            Long cid2 = productQuery.getCid2();
            if (cid2 != null) {
                Predicate p = cb.equal(root.get("cid2"), cid2);
                predicateList.add(p);
            }

            return cb.and(ArrayUtil.toArray(predicateList, Predicate.class));
        };

        return JpaUtil.pageConvert(this.productDao.findAll(specification, pageRequest), ProductDto.class);
    }
}
