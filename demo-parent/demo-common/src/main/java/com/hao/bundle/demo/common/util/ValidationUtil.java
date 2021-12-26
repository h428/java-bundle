package com.hao.bundle.demo.common.util;

import com.hao.bundle.demo.common.exception.BaseException;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * 校验工具类，依赖 Hibernate Validator，可基于 java.validation 注解对 pojo 做校验
 */
public class ValidationUtil {

    // 为了通用性，这里自己初始化校验器而不进行注入，这样子类可以不依赖 Spring 环境，对于 boot-ssm 直接注入也是可以的
    private static final Validator validator;

    static {
        // 初始化方式一：直接使用 Spring 容器中的 Validator，依赖 Spring 环境，有 Spring 环境时可采用该种方式
        // validator = SpringContextUtil.getBean(Validator.class);

        // 初始化方式二：自行初始化 validator，不依赖 spring 环境，但需要确保引入 hibernate-validator 包
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        validator = vf.getValidator();
    }

    public static <T> void validate(T entity, Class<?>... groups) {
        // 基于 Add 分组校验实体，如果存在错误直接抛出 ExcelParseException 异常
        Set<ConstraintViolation<T>> constraintViolationSet = validator.validate(entity, groups);
        if (constraintViolationSet != null && !constraintViolationSet.isEmpty()) {
            // 短路：抛出全局通用异常
            for (ConstraintViolation<T> constraintViolation : constraintViolationSet) {
                throw new BaseException(constraintViolation.getMessage());
            }
        }
    }

    public static void main(String[] args) {

    }

}
