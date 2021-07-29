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

// 使用邮箱进行登录
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDTO extends PojoConverter<LoginUserDTO, User> {

    @NotBlank(message = "登录邮箱不能为空")
//    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "邮箱格式不正确") // 自己校验邮箱
    @Email(message = "邮箱格式不正确") // 使用 @Email 校验邮箱
    private String email;

    @NotBlank(message = "密码不能为空")
    private String userPass;

    private String checkCode; // 暂时不添加校验

}
