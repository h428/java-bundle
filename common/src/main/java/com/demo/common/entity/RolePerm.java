package com.demo.common.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "role_perm")
public class RolePerm implements Serializable {

    @Id
    @Column(name = "role_id")
    private Long roleId; // 角色 id

    @Id
    @Column(name = "perm_id")
    private Long permId; // 权限 id
}