package com.demo.common.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Perm implements Serializable {
    @Id
    private Long id;

    private String name; // 权限名称

    private String tag;

    private Long pid;

    private Boolean nav;
}