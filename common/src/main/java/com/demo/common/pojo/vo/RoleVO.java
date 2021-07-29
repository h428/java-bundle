package com.demo.common.pojo.vo;

import com.demo.common.entity.Perm;
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
public class RoleVO {
    private Long id;

    private String name; // 角色名称

    private List<Perm> permList;
}
