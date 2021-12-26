package com.hao.bundle.demo.common.aop.anno;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Perm {
    String value();
}
