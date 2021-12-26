package com.hao.bundle.demo.web.interceptor;

import cn.hutool.core.util.StrUtil;
import com.google.common.net.HttpHeaders;
import com.hao.bundle.demo.common.component.RedisUtil;
import com.hao.bundle.demo.common.component.TokenUtil;
import com.hao.bundle.demo.common.threadlocal.UserIdThreadLocal;
import com.hao.bundle.demo.common.util.AjaxUtil;
import com.hao.bundle.demo.pojo.wrapper.Response;
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
            // 特别注意，采用直接抛出异常的做法，若是用户访问的 url 为非法 url（Controller 中没有声明），
            // 该异常将无法被全局异常处理器处理而会被直接抛出到用户
            // throw new BaseException(HttpStatus.HTTP_UNAUTHORIZED, "未登录");

            // 因此对于全局过滤器，建议的做法是直接回写响应而不要利用全局异常
            // 这样，即使访问的是 Controller 中没有声明的 url，也会被登录拦截器过滤并正确给出 json 结果
            Response<Object> unauthorized = Response.unauthorized_401("未登录");
            AjaxUtil.writeObject(response, 200, unauthorized);
            return false;
        }

        UserIdThreadLocal.set(Long.valueOf(baseUserIdStr));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserIdThreadLocal.remove();
    }
}
