package com.hao.bundle.demo.common.pojo.constant;


import com.hao.bundle.demo.common.enums.Regexp;

public interface UserConstant {

    /**
     * 邮箱
     */
    String EMAIL_NOT_BLANK_MESSAGE = "邮箱不能为空";
    String EMAIL_MESSAGE = "邮箱格式不正确";

    /**
     * 用户名
     */
    String USER_NAME_NOT_BLANK_MESSAGE = "用户名不能为空";
    String USER_NAME_PATTERN_REGEXP = Regexp.USER_NAME;
    String USER_NAME_PATTERN_MESSAGE = "户名只能包含中英文、数字、下划线、减号，长度必须在 2 - 16 之间";

    /**
     * 密码
     */
    String USER_PASS_NOT_BLANK_MESSAGE = "密码不能为空";
    String USER_PASS_PATTERN_REGEXP = Regexp.USER_PASS;
    String USER_PASS_PATTERN_MESSAGE = "密码必须同时包含字母和数字，长度至少为8，若有特殊字符则只允许 @#$()+-*/%^&,.?! 这几个特殊字符";


    /**
     * 验证码
     */
    String CAPTCHA_NOT_BLANK = "验证码不能为空";
    String CAPTCHA_PATTERN_REGEXP = Regexp.CAPTCHA;
    String CAPTCHA_PATTERN_MESSAGE = "验证码格式不正确";
}
