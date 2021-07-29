package com.demo.common.exception;

/**
 * 插入数据错误异常，400
 */
public class InsertErrorException extends BaseWebException {

    public InsertErrorException(String message) {
        super(400, message);
    }
}
