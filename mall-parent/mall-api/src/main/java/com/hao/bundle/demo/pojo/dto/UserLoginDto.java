package com.hao.bundle.demo.pojo.dto;

import com.hao.bundle.demo.pojo.constant.UserConstant;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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
public class UserLoginDto {

    /**
     * 电子邮箱
     */
    @NotBlank(message = UserConstant.EMAIL_NOT_BLANK_MESSAGE)
    @Email(message = UserConstant.EMAIL_MESSAGE)
    private String email;

    /**
     * 密码
     */
    @NotBlank(message = UserConstant.USER_PASS_NOT_BLANK_MESSAGE)
    private String userPass;

    /**
     * 验证码
     */
    private String captcha; // 暂时不添加校验

}
