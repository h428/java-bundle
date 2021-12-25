package com.hao.bundle.demo.common.util;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AjaxUtil {

    public static final String DOMAIN_NAME = "*";

    public static void writeObject(ServletResponse response, int status, Object object) {
        String json = JacksonUtil.toJson(object);
        writeJsonResponse(response, status, json);
    }

    public static void writeJsonResponse(ServletResponse response, int status, String json) {
        try {
            // 转化为 HttpServletResponse
            HttpServletResponse resp = (HttpServletResponse) response;

            // 设置跨域支持
            resp.setHeader("Access-Control-Allow-Origin", "*");
            resp.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
            // 设置响应状态码
            resp.setStatus(status);
            // 设置响应类型为 json
            response.setContentType("application/json;charset=UTF-8");
            // 写入 json
            PrintWriter writer = response.getWriter();
            writer.write(json);
            writer.close(); // 是否关闭输出流有待斟酌
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String AJAX_HEADER = "XMLHttpRequest";

    /**
     * 判断是否 ajax 请求
     *
     * @param request 请求对象
     * @return 是 ajax 请求返回 true；不是则返回 false
     */
    public static boolean isAjaxRequest(ServletRequest request) {
        String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
        if (AJAX_HEADER.equalsIgnoreCase(header)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}