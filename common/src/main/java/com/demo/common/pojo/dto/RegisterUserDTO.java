package com.demo.common.pojo.dto;

import com.demo.common.entity.User;
import com.demo.common.pojo.converter.PojoConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

// 注册实体
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDTO extends PojoConverter<RegisterUserDTO, User> {

    @NotBlank(message = "邮箱不能为空")
    @Email
    private String email;

    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,16}$", message = "用户名只能由字母、数字、下划线构成，长度至少为 3，不得超过 16")
    private String userName;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@#$()+\\-*/%^&,.?!]{8,}$",
            message = "密码必须同时包含字母和数字，长度至少为8，若有特殊字符则只允许 @#$()+-*/%^&,.?! 这几个特殊字符")
    private String userPass;

    @NotBlank(message = "确认密码不能为空")
    private String confirmPass;
}
