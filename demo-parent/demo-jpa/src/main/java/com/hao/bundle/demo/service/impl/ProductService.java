package com.hao.bundle.demo.service.impl;

import com.hao.bundle.demo.dao.ProductDao;
import com.hao.bundle.demo.entity.Product;
import com.hao.bundle.demo.pojo.converter.ProductConverter;
import com.hao.bundle.demo.pojo.dto.ProductDto;
import com.hao.bundle.demo.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
