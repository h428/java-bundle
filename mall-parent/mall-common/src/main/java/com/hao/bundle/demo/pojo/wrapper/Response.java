package com.hao.bundle.demo.pojo.wrapper;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Builder
@ToString
@ApiModel("通用响应体")
public class Response<T> {

    @ApiModelProperty("状态码")
    private int status;

    @ApiModelProperty("提示信息")
    private String message;

    @ApiModelProperty("响应数据")
    private T data;


    // 给定状态码和提示消息构建响应体
    public static <T> Response<T> build(int status, String message, T data) {
        return Response.<T>builder().status(status).message(message).data(data).build();
    }

    // 200
    public static <T> Response<T> ok_200(String message, T data) {
        return Response.build(OK, message, data);
    }

    public static <T> Response<T> ok_200(String message) {
        return Response.build(OK, message, null);
    }

    public static <T> Response<T> ok_200(T data) {
        return Response.build(OK, "请求通过", data);
    }


    public static <T> Response<T> ok_200() {
        return Response.build(OK, "请求通过", null);
    }


    // 400
    public static <T> Response<T> badRequest_400(String message) {
        return Response.build(BAD_REQUEST, message, null);
    }

    // 401
    public static <T> Response<T> unauthorized_401(String message) {
        return Response.build(UNAUTHORIZED, message, null);
    }

    public static <T> Response<T> unauthorized_401() {
        return Response.build(UNAUTHORIZED, "未认证", null);
    }

    // 403
    public static <T> Response<T> forbidden_403(String message) {
        return Response.build(FORBIDDEN, message, null);
    }

    public static <T> Response<T> forbidden_403() {
        return Response.build(FORBIDDEN, "权限不足", null);
    }

    // 404
    public static <T> Response<T> not_found_404(String msg) {
        return Response.build(NOT_FOUND, "资源不存在", null);
    }

    public static <T> Response<T> not_found_404() {
        return Response.build(NOT_FOUND, "资源不存在", null);
    }


    // 409
    public static <T> Response<T> conflict_409(String msg) {
        return Response.build(CONFLICT, "请求冲突", null);
    }

    public static <T> Response<T> conflict_409() {
        return Response.build(CONFLICT, "请求冲突", null);
    }


    // 500
    public static <T> Response<T> internal_server_error_500(String msg) {
        return Response
            .build(INTERNAL_SERVER_ERROR, "服务器异常", null);
    }

    public static <T> Response<T> internal_server_error_500() {
        return Response
            .build(INTERNAL_SERVER_ERROR, "服务器异常", null);
    }
    
    
    // 常用通用状态码
    private static final int OK = 200;
    private static final int BAD_REQUEST = 400;
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int CONFLICT = 409;
    private static final int INTERNAL_SERVER_ERROR = 500;
    
}
