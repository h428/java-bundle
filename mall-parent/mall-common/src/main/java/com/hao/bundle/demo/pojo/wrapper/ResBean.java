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
public class ResBean<T> {

    @ApiModelProperty("状态码")
    private int status;

    @ApiModelProperty("提示信息")
    private String message;

    @ApiModelProperty("响应数据")
    private T data;


    // 给定状态码和提示消息构建响应体
    public static <T> ResBean<T> build(int status, String message, T data) {
        return ResBean.<T>builder().status(status).message(message).data(data).build();
    }

    // 200
    public static <T> ResBean<T> ok_200(String message, T data) {
        return ResBean.build(OK, message, data);
    }

    public static <T> ResBean<T> ok_200(String message) {
        return ResBean.build(OK, message, null);
    }

    public static <T> ResBean<T> ok_200(T data) {
        return ResBean.build(OK, "请求通过", data);
    }


    public static <T> ResBean<T> ok_200() {
        return ResBean.build(OK, "请求通过", null);
    }


    // 400
    public static <T> ResBean<T> badRequest_400(String message) {
        return ResBean.build(BAD_REQUEST, message, null);
    }

    // 401
    public static <T> ResBean<T> unauthorized_401(String message) {
        return ResBean.build(UNAUTHORIZED, message, null);
    }

    public static <T> ResBean<T> unauthorized_401() {
        return ResBean.build(UNAUTHORIZED, "未认证", null);
    }

    // 403
    public static <T> ResBean<T> forbidden_403(String message) {
        return ResBean.build(FORBIDDEN, message, null);
    }

    public static <T> ResBean<T> forbidden_403() {
        return ResBean.build(FORBIDDEN, "权限不足", null);
    }

    // 404
    public static <T> ResBean<T> not_found_404(String msg) {
        return ResBean.build(NOT_FOUND, "资源不存在", null);
    }

    public static <T> ResBean<T> not_found_404() {
        return ResBean.build(NOT_FOUND, "资源不存在", null);
    }


    // 409
    public static <T> ResBean<T> conflict_409(String msg) {
        return ResBean.build(CONFLICT, "请求冲突", null);
    }

    public static <T> ResBean<T> conflict_409() {
        return ResBean.build(CONFLICT, "请求冲突", null);
    }


    // 500
    public static <T> ResBean<T> internal_server_error_500(String msg) {
        return ResBean
            .build(INTERNAL_SERVER_ERROR, "服务器异常", null);
    }

    public static <T> ResBean<T> internal_server_error_500() {
        return ResBean
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
