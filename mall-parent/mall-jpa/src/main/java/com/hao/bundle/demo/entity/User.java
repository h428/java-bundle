package com.hao.bundle.demo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Table
@DynamicInsert
@DynamicUpdate
public class User {

    @Id
    @GenericGenerator(name = "snowflakeIdGenerator", strategy = "com.hao.bundle.demo.common.component.SnowflakeIdGenerator")
    @GeneratedValue(generator = "snowflakeIdGenerator")
    private Long id;

    private String email;

    private String userName;

    private String userPass;

    private String salt;

    private Double height;

    private String avatar;

    private Integer score;

    private LocalDate birthday;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Date lastLoginTime;

    private Integer status;

    private Long roleId;

    private Boolean deleted;

    private Date deleteTime;
}
