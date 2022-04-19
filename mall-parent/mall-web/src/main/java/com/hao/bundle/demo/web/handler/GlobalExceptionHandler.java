package com.hao.bundle.demo.web.handler;

import com.hao.bundle.demo.common.exception.BaseException;
import com.hao.bundle.demo.pojo.wrapper.ResBean;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理器，特别注意只会处理合法 url 内的异常（Controller 中声明的 url）；
 * 若是非法 url （未在 Controller 中声明的 url）且被拦截器拦截，拦截器内部抛出的异常不会被该处理器处理；
 * 但若是合法的 url，被拦截器拦截且带拦截器内部抛出的异常会被该处理器处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // 统一处理继承于 BaseException 的全局异常（包含了状态码）
    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public ResBean<?> handleBaseException(BaseException ex) {
        return ResBean.build(ex.getStatus(), ex.getMessage(), null);
    }

    // RequestBody 类请求校验失败产生的异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResBean<?> handleBindException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        return ResBean.badRequest_400(fieldError.getDefaultMessage());
    }

    // 表单类请求校验失败产生的异常
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResBean<?> handleBindException(BindException ex) {
        //校验 除了 requestBody 注解方式的参数校验 对应的 bindingResult 为 BeanPropertyBindingResult
        FieldError fieldError = ex.getBindingResult().getFieldError();
        return ResBean.badRequest_400(fieldError.getDefaultMessage());
    }

    // 路径参数绑定失败产生的异常
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResBean<?> handleBindException(ConstraintViolationException ex) {
        ConstraintViolation<?> constraintViolation = ex.getConstraintViolations().iterator().next();
        return ResBean.badRequest_400(constraintViolation.getMessage());
    }
}
