package com.hao.bundle.demo.web.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.google.common.net.HttpHeaders;
import com.hao.bundle.demo.common.component.RedisUtil;
import com.hao.bundle.demo.common.component.TokenUtil;
import com.hao.bundle.demo.common.exception.BaseException;
import com.hao.bundle.demo.common.threadlocal.UserIdThreadLocal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 对 option 请求放行，用于 ajax 跨域
        String method = request.getMethod();
        if (HttpMethod.OPTIONS.name().equals(method)) {
            return true;
        }

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        String key = TokenUtil.USER_TOKEN_PREFIX + token;

        String baseUserIdStr = RedisUtil.get(key);

        // redis 不存在对应的 token，表示未登录
        if (StrUtil.isEmpty(baseUserIdStr)) {
            throw new BaseException(HttpStatus.HTTP_UNAUTHORIZED, "未登录");
        }

        UserIdThreadLocal.set(Long.valueOf(baseUserIdStr));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserIdThreadLocal.remove();
    }
}
