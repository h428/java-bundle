package com.hao.bundle.demo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Column;
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

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_pass")
    private String userPass;

    private String salt;

    private Double height;

    private String avatar;

    private Integer score;

    private LocalDate birthday;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "last_login_time")
    private Date lastLoginTime;

    private Integer status;

    @Column(name = "role_id")
    private Long roleId;

    private Boolean deleted;

    @Column(name = "delete_time")
    private Date deleteTime;
}
