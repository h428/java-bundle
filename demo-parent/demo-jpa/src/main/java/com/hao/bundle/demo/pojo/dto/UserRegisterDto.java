package com.hao.bundle.demo.pojo.dto;

import com.hao.bundle.demo.common.exception.ParamErrorException;
import com.hao.bundle.demo.common.pojo.constant.UserConstant;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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
public class UserRegisterDto {


    /**
     * 邮箱
     */
    @NotBlank(message = UserConstant.EMAIL_NOT_BLANK_MESSAGE)
    @Email(message = UserConstant.EMAIL_MESSAGE)
    private String email;

    /**
     * 用户名
     */
    @NotBlank(message = UserConstant.USER_NAME_NOT_BLANK_MESSAGE)
    @Pattern(regexp = UserConstant.USER_NAME_PATTERN_REGEXP, message = UserConstant.USER_NAME_PATTERN_MESSAGE)
    private String userName;

    /**
     * 密码
     */
    @NotBlank(message = UserConstant.USER_PASS_NOT_BLANK_MESSAGE)
    @Pattern(regexp = UserConstant.USER_PASS_PATTERN_REGEXP, message = UserConstant.USER_PASS_PATTERN_MESSAGE)
    private String userPass;

    /**
     * 确认密码
     */
    @NotBlank(message = CONFIRM_PASS_NOT_BLANK)
    private String confirmPass;

    public static final String CONFIRM_PASS_NOT_BLANK = "确认密码不能为空";

    /**
     * 验证码
     */
    @NotBlank(message = UserConstant.CAPTCHA_NOT_BLANK)
    @Pattern(regexp = UserConstant.CAPTCHA_PATTERN_REGEXP, message = UserConstant.CAPTCHA_PATTERN_MESSAGE)
    private String captcha;



    /**
     * 补充的参数校验
     */
    public void validatePassword() {
        // 校验两次密码相同
        if (!this.getUserPass().equals(this.getConfirmPass())) {
            throw new ParamErrorException("两次输入的密码不相同");
        }
    }

}
