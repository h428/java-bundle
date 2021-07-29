package com.demo.common.pojo.dto;

import com.demo.common.entity.Role;
import com.demo.common.pojo.converter.PojoConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO extends PojoConverter<RoleDTO, Role> {
    private Long id;

    private String name; // 角色名称

    private List<Long> permIdList;
}
