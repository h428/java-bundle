package com.demo.boot.ssm.web.interceptor;

import com.demo.base.component.pojo.bean.ResBean;
import com.demo.base.component.util.AjaxUtil;
import com.demo.base.component.util.JwtUtil;
import com.demo.boot.ssm.bean.thredlocal.UserIdThreadLocal;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {


    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 对 option 请求放行，用于 ajax 跨域
        String method = request.getMethod();
        if ("OPTIONS".equals(method)) {
            return true;
        }

        // 打印请求地址
        System.out.println("request url : " + request.getRemoteAddr() + ":"
                + request.getRemotePort() + request.getRequestURI());

        String token = request.getHeader("Authorization");


//        System.out.println(token);

        if (StringUtils.isEmpty(token)) {
            AjaxUtil.writeJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, objectMapper.writeValueAsString(new ResBean(401, "未携带 token")));
            return false;
        }

        try {
            String id = JwtUtil.parseTokenReturnSubject(token);
            // id 放到当前线程
            UserIdThreadLocal.set(Long.valueOf(id));

            return true;
        } catch (JwtException ex) {
            // token 解析异常不予以放行，
            AjaxUtil.writeJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, objectMapper.writeValueAsString(new ResBean(401, "token 过期或者 token 无效")));
            return false;
        }
    }
}