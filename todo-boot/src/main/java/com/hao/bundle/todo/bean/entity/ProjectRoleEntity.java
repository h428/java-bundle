package com.hao.bundle.todo.bean.entity;

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
@Table(name = "project_role")
@DynamicInsert
@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class ProjectRoleEntity {

    @Id
    @Column(name = "id")
    @GenericGenerator(name = "snowflakeIdGenerator", strategy = "com.hao.bundle.todo.common.component.SnowflakeIdGenerator")
    @GeneratedValue(generator = "snowflakeIdGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "info")
    private String info;

    @Column(name = "project_perm")
    private String projectPerm;

    @Column(name = "create_user_id")
    private Long createUserId;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "update_user_id")
    private Long updateUserId;

    @Column(name = "update_time")
    private Long updateTime;

}
