package com.hao.bundle.demo.web.handler;

import com.hao.bundle.demo.common.exception.BaseException;
import com.hao.bundle.demo.common.pojo.wrapper.Response;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 统一处理继承于 BaseException 的全局异常（包含了状态码）
    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public Response<?> handleBaseException(HttpServletResponse response, BaseException ex) {
        return Response.build(ex.getStatus(), ex.getMessage(), null);
    }

    // RequestBody 类请求校验失败产生的异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Response<?> handleBindException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        return Response.badRequest_400(fieldError.getDefaultMessage());
    }

    // 表单类请求校验失败产生的异常
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Response<?> handleBindException(BindException ex) {
        //校验 除了 requestBody 注解方式的参数校验 对应的 bindingResult 为 BeanPropertyBindingResult
        FieldError fieldError = ex.getBindingResult().getFieldError();
        return Response.badRequest_400(fieldError.getDefaultMessage());
    }

    // 路径参数绑定失败产生的异常
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Response<?> handleBindException(ConstraintViolationException ex) {
        ConstraintViolation<?> constraintViolation = ex.getConstraintViolations().iterator().next();
        return Response.badRequest_400(constraintViolation.getMessage());
    }
}
