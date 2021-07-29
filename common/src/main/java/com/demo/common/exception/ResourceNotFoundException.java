package com.demo.common.exception;

/**
 * 资源不存在抛出的异常，可以给出提示或使用默认提示，最后由全局异常处理器处理为 404
 */
public class ResourceNotFoundException extends BaseWebException {


    public ResourceNotFoundException(String message) {
        super(404, message);
    }
}
