package com.demo.common.mapper2;

import com.demo.common.entity.Product;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AddProductMapper {
    @Select("select * from product where match(name, description, detail) against('+${keyword}' in boolean mode)")
    List<Product> searchProductByKeyword(@Param("keyword") String keyword);
}
