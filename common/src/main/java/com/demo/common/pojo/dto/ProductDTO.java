package com.demo.common.pojo.dto;

import com.demo.common.entity.Product;
import com.demo.common.pojo.converter.PojoConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO extends PojoConverter<ProductDTO, Product> {

    private Long id;

    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotNull(message = "价格不能为空")
    private Float price;

    private String description;

    private String detail;

    private String images;

    private String note;

    private Integer status;

    private boolean saved; // 导入数据时用到的标记，其他时候无作用

    @NotNull(message = "一级分类不能为空")
    private Long cid1;

    @NotNull(message = "二级分类不能为空")
    private Long cid2;

    private Boolean deleted;

    private Long delTime;

}
