package com.hao.bundle.demo.pojo.converter;

import com.hao.bundle.demo.entity.Perm;
import com.hao.bundle.demo.pojo.dto.PermDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PermConverter {

    PermConverter INSTANCE = Mappers.getMapper(PermConverter.class);

    List<PermDto> entityToDto(List<Perm> permList);

}
