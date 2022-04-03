package com.hao.bundle.demo.pojo.dto;

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
public class ProductDto {

    private Long id;

    private String name;

    private Float price;

    private String description;

    private String detail;

    private String images;

    private String note;

    private Integer status;

    private Long cid1;

    private Long cid2;

}
