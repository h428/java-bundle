package com.hao.bundle.demo.pojo.converter;

import com.hao.bundle.demo.entity.Perm;
import com.hao.bundle.demo.pojo.dto.PermDto;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface PermConverter {

    List<PermDto> entityToDto(List<Perm> permList);

}
