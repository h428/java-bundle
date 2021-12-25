package com.hao.bundle.todo.bean.dto;

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
public class BaseUserRegisterDTO {

    private String email;

    private String userName;

    private String userPass;

    private String confirmPass;

    private String captcha;

}
