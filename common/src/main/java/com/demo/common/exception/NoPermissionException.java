package com.demo.common.exception;

/**
 * 权限不足异常，403
 */
public class NoPermissionException extends BaseWebException{

    public NoPermissionException(String message) {
        super(403, message);
    }

}
