package com.hao.bundle.demo.pojo.converter;

import com.hao.bundle.demo.entity.Product;
import com.hao.bundle.demo.pojo.dto.ProductDto;
import com.hao.bundle.demo.pojo.query.ProductQuery;
import org.mapstruct.Mapper;

@Mapper
public interface ProductConverter {

    ProductDto entityToDto(Product product);

    Product queryToEntity(ProductQuery productQuery);

}
