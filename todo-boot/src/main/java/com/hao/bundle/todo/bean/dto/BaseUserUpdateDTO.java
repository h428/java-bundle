package com.hao.bundle.todo.bean.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class BaseUserUpdateDTO {

    private Long id;

    private String email;

    private String userName;

    private Date birthday;

    private Double height;

    private String avatar;

}
