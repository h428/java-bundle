package com.hao.bundle.demo.common.enums;

import java.util.regex.Pattern;

public interface Regexp {

    String EMAIL = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    String IMG = "^.+\\.(?i)(jpg|jpeg|png)$";
    String EXCEL = "^.+\\.(?i)(xls|xlsx)$";
    String EXCEL_2003 = "^.+\\.(?i)(xls)$";
    // 用户名只能由字母、数字、下划线构成，长度至少为 3，不得超过 16
    String USER_NAME = "^[a-zA-Z0-9_]{3,16}$";
    // 密码必须同时包含字母和数字，长度至少为8，若有特殊字符则只允许 @#$()+-*/%^&,.?! 这几个特殊字符
    String USER_PASS = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@#$()+\\-*/%^&,.?!]{8,}$";
    // 验证按由 6 位数字组成
    String CAPTCHA = "^[0-9]{6}$";

    enum Matcher {
        EMAIL(Regexp.EMAIL),
        IMG(Regexp.IMG),
        EXCEL(Regexp.EXCEL),
        EXCEL_2003(Regexp.EXCEL_2003),
        USER_NAME(Regexp.USER_NAME),
        USER_PASS(Regexp.USER_PASS),
        CAPTCHA(Regexp.CAPTCHA),
        ;

        private final Pattern pattern;

        Matcher(String regexp) {
            this.pattern = Pattern.compile(regexp);
        }

        public boolean matches(String input) {
            return pattern.matcher(input).matches();
        }
    }

}
