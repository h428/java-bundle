package com.demo.common.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product implements Serializable {
    @Id
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

    private Boolean deleted;

    @Column(name = "del_time")
    private Long delTime;

}