package com.hao.bundle.demo.pojo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Builder
@ToString
public class UserDto {

    private Long id;

    private String email;

    private String userName;

    private Double height;

    private String avatar;

    private Integer score;

    private LocalDate birthday;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Date lastLoginTime;

    private Integer status;

    private Long roleId;

}
