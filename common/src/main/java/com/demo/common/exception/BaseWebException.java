package com.demo.common.exception;

import lombok.Getter;

// 基础全局异常类，继承该类的异常都会被 Spring 的全局异常处理器捕捉，并转化为 ResBean 对应的 json 形式
@Getter
public abstract class BaseWebException extends RuntimeException{

    private int status; // 响应状态码

    // status 为状态码，message 为提示消息
    public BaseWebException(Integer status, String message) {
        super(message);
        this.status = status;
    }
}
