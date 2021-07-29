package com.demo.common.exception;

/**
 * 更新失败异常，400
 */
public class DeleteErrorException extends BaseWebException {

    public DeleteErrorException(String message) {
        super(400, message);
    }
}
