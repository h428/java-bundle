package com.demo.common.exception;

/**
 * 服务器内部错误抛出的异常，最后由全局异常转化为 500 代码
 */
public class InternalServerErrorException extends BaseWebException {

    public InternalServerErrorException(String message) {
        super(500, message);
    }
}
