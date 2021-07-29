package com.demo.common.exception;

/**
 * DTO 参数校验出错抛出的异常，必须提供出错提示，最后由全局异常转化为 400 代码
 */
public class ParamErrorException extends BaseWebException {


    public ParamErrorException(String message) {
        super(400, message);
    }
}
