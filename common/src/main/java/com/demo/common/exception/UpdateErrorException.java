package com.demo.common.exception;

/**
 * 更新失败异常，400
 */
public class UpdateErrorException extends BaseWebException {

    public UpdateErrorException(String message) {
        super(400, message);
    }
}
