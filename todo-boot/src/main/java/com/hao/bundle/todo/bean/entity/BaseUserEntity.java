package com.hao.bundle.todo.bean.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "base_user")
@DynamicInsert
@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class BaseUserEntity {

    @Id
    @Column(name = "id")
    @GenericGenerator(name = "snowflakeIdGenerator", strategy = "com.hao.bundle.todo.common.component.SnowflakeIdGenerator")
    @GeneratedValue(generator = "snowflakeIdGenerator")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_pass")
    private String userPass;

    @Column(name = "salt")
    private String salt;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "height")
    private Double height;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "register_time")
    private Date registerTime;

    @Column(name = "last_update_time")
    private Date lastUpdateTime;

    @Column(name = "login_time")
    private Date loginTime;

    @Column(name = "status")
    private Integer status;

    @Column(name = "deleted")
    private Boolean deleted;

}
