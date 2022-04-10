package com.hao.bundle.demo.pojo.converter;

import com.hao.bundle.demo.entity.Product;
import com.hao.bundle.demo.pojo.dto.ProductDto;
import com.hao.bundle.demo.pojo.query.ProductQuery;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductConverter {

    ProductConverter INSTANCE = Mappers.getMapper(ProductConverter.class);

    ProductDto entityToDto(Product product);

    Product queryToEntity(ProductQuery productQuery);

}
