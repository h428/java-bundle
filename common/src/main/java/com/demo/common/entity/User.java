package com.demo.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable {

    @Id
    private Long id; // 主键

    private String email; // 电子邮箱

    @Column(name = "user_name")
    private String userName; // 用户名

    @Column(name = "user_pass")
    @JsonIgnore
    private String userPass; // 密码

    @JsonIgnore
    private String salt; // 盐值

    private Integer age; // 年龄

    private Double height; // 身高

    private String avatar; // 头像

    private Integer status; // 状态

    @Column(name = "role_id")
    private Long roleId; // 角色 id

    @Column(name = "register_time")
    private Date registerTime; // 注册时间

    @Column(name = "last_update_time")
    private Date lastUpdateTime;

    @Column(name = "login_time")
    private Date loginTime; // 上次登录时间

    private Boolean deleted; // 删除标记
}