package com.demo.common.pojo.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

// 基于 ResponseEntity 的常用响应对象
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResBean implements Serializable {

    @ApiModelProperty(name = "status", value = "响应状态码")
    private int status;
    @ApiModelProperty(name = "message", value = "提示消息")
    private String message;


    // 给定状态码和提示消息构建响应体
    public static ResponseEntity<ResBean> build(int status, String msg) {
        return ResponseEntity.status(status).body(new ResBean(status, msg));
    }

    // 200
    public static ResponseEntity<ResBean> ok_200(String msg) {
        return ResponseEntity.ok(new ResBean(200, msg));
    }

    public static ResponseEntity<ResBean> ok_200() {
        return ok_200("请求成功");
    }

    // 400
    public static ResponseEntity<ResBean> badRequest_400(String msg) {
        return ResponseEntity.badRequest().body(new ResBean(400, msg));
    }

    public static ResponseEntity badRequest_400() {
        return badRequest_400("请求参数有误");
    }

    // 401
    public static ResponseEntity<ResBean> unauthorized_401(String msg) {
        return ResponseEntity.status(401).body(new ResBean(401, msg));
    }

    public static ResponseEntity<ResBean> unauthorized_401() {
        return unauthorized_401("未登录");
    }

    // 403
    public static ResponseEntity<ResBean> forbidden_403(String msg) {
        return ResponseEntity.status(403).body(new ResBean(403, msg));
    }
    public static ResponseEntity<ResBean> forbidden_403() {
        return forbidden_403("权限不足");
    }

    // 404
    public static ResponseEntity<ResBean> not_found_404(String msg) {
        return ResponseEntity.status(404).body(new ResBean(404, msg));
    }
    public static ResponseEntity<ResBean> not_found_404() {
        return not_found_404("资源不存在");
    }


    // 409
    public static ResponseEntity<ResBean> conflict_409(String msg) {
        return ResponseEntity.status(404).body(new ResBean(409, msg));
    }
    public static ResponseEntity<ResBean> conflict_409() {
        return conflict_409("发生冲突，请求失败");
    }


    // 500
    public static ResponseEntity<ResBean> internal_server_error_500(String msg) {
        return ResponseEntity.status(500).body(new ResBean(500, msg));
    }

    public static ResponseEntity<ResBean> internal_server_error_500() {
        return internal_server_error_500("服务器内部错误");
    }

    public static void main(String[] args) {

    }

}
