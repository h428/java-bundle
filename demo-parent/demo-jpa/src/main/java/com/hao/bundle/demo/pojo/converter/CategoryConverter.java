package com.hao.bundle.demo.pojo.converter;

import com.hao.bundle.demo.entity.Category;
import com.hao.bundle.demo.pojo.dto.CategoryDto;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryConverter {

    List<CategoryDto> entityToDto(List<Category> categoryList);

    CategoryDto entityToDto(Category Category);

}
