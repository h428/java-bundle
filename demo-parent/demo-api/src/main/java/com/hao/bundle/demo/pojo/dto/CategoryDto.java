package com.hao.bundle.demo.pojo.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Builder
@ToString
public class CategoryDto {

    private Long id;

    private String name;

    private Long pid;

    private List<CategoryDto> children;

}
