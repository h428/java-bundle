package com.demo.common.exception;

/**
 * excel 解析异常，400
 */
public class ExcelParseException extends BaseWebException {

    public ExcelParseException(String message) {
        // 400 为状态码，
        super(400, message);
    }

}
