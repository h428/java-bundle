package com.hao.bundle.demo.pojo.converter;

import com.hao.bundle.demo.entity.Category;
import com.hao.bundle.demo.pojo.dto.CategoryDto;
import com.hao.bundle.demo.pojo.dto.CategorySaveDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryConverter {

    CategoryConverter INSTANCE = Mappers.getMapper(CategoryConverter.class);

    Category saveDtoToEntity(CategorySaveDto categorySaveDto);

    List<CategoryDto> entityToDto(List<Category> categoryList);

    CategoryDto entityToDto(Category category);

}
