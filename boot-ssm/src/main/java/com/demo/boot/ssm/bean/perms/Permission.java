package com.demo.boot.ssm.bean.perms;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permission {
    String value() default "默认值";
}
