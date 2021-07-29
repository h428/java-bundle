package com.demo.common.util;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AjaxUtil {

    public static final String DOMAIN_NAME = "*";

    public static void writeJsonResponse(ServletResponse response, int statusCode, String json) throws IOException {
        // 转化为 HttpServletResponse
        HttpServletResponse resp = (HttpServletResponse) response;

        // 设置跨域支持
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        // 设置响应状态码
        resp.setStatus(statusCode);
        // 设置响应类型为 json
        response.setContentType("application/json;charset=UTF-8");
        // 写入 json
        PrintWriter writer = response.getWriter();
        writer.write(json);
        // TODO 是否关闭输出流有待斟酌
//        writer.close();
    }

    private boolean isAjax(ServletRequest request) {
        String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
        if ("XMLHttpRequest".equalsIgnoreCase(header)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
