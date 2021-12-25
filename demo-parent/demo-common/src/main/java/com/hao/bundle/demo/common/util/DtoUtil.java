package com.hao.bundle.demo.common.util;

import com.hao.bundle.demo.common.exception.BaseException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class DtoUtil {

    /**
     * 将 bindingResult 转化为通用异常
     *
     * @param bindingResult spring 校验器的绑定结果
     */
    public static void checkDtoParams(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            throw new BaseException(fieldError.getField() + ", " + fieldError.getDefaultMessage());
        }
    }
}
