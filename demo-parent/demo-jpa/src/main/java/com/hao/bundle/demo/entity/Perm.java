package com.hao.bundle.demo.entity;

import javax.persistence.Entity;
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
public class Perm {

    /**
     * 不设置 @GeneratedValue，等价于 AUTO，由程序选择主键生成策略；
     * 由于采用 MySQL 数据库，该表没有开启 AUTO_INCREMENT，将会采用手动设置 方式
     */
    @Id
    private Long id;

    private String name;

    private String tag;

    private Long pid;

    private Boolean nav;

}
