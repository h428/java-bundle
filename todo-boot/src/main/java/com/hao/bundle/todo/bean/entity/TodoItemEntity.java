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
@Table(name = "todo_item")
@DynamicInsert
@DynamicUpdate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class TodoItemEntity {

    @Id
    @Column(name = "id")
    @GenericGenerator(name = "snowflakeIdGenerator", strategy = "com.hao.bundle.todo.common.component.SnowflakeIdGenerator")
    @GeneratedValue(generator = "snowflakeIdGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "content")
    private String content;

    @Column(name = "text_type")
    private Integer textType;

    @Column(name = "attachment")
    private String attachment;

    @Column(name = "cat1")
    private Integer cat1;

    @Column(name = "cat2")
    private Integer cat2;

    @Column(name = "cat3")
    private Integer cat3;

    @Column(name = "create_user_id")
    private Long createUserId;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "update_user_id")
    private Long updateUserId;

    @Column(name = "update_time")
    private Long updateTime;
    
}
