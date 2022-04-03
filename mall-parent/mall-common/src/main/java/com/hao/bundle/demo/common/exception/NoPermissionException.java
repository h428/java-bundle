package com.hao.bundle.demo.common.exception;

public class NoPermissionException extends BaseException {

    public NoPermissionException(String message) {
        super(403, message);
    }

    public NoPermissionException() {
        super(403, "权限不足");
    }
}
