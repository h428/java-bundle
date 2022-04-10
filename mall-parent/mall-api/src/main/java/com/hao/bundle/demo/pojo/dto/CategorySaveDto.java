package com.hao.bundle.demo.pojo.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class CategorySaveDto {

    private Long id;

    @NotEmpty(message = "分类名称不能为空")
    private String name;

    @NotNull(message = "父级分类 id 不能为空")
    private Long pid;

}
