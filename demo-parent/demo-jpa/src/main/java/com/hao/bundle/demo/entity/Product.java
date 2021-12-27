package com.hao.bundle.demo.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Builder
@ToString
@Entity
@DynamicInsert
@DynamicUpdate
public class Product {

    @Id
    @GenericGenerator(name = "snowflakeIdGenerator", strategy = "com.hao.bundle.demo.common.component.SnowflakeIdGenerator")
    @GeneratedValue(generator = "snowflakeIdGenerator")
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

    // 支持自动驼峰和下划线的映射
    private LocalDateTime deleteTime;

}
