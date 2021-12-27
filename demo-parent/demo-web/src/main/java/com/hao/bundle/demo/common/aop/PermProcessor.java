package com.hao.bundle.demo.common.aop;

import com.hao.bundle.demo.cache.PermCache;
import com.hao.bundle.demo.common.aop.anno.Perm;
import com.hao.bundle.demo.common.exception.NoPermissionException;
import com.hao.bundle.demo.common.threadlocal.UserIdThreadLocal;
import java.lang.reflect.Method;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

@Aspect
@Component
public class PermProcessor {


    /**
     * 定义切面：com.hao.bundle.demo.web.controller 包下的所有类的所有方法
     */
    @Pointcut("execution(public * com.hao.bundle.demo.web.controller.*.*(..))")
    public void aspect() {

    }

    /**
     * 权限环绕通知
     */
    @ResponseBody
    @Around("aspect()")
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取 aop 拦截的方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();

        // 取得方法上 @Perm 注解的访问权限值，若没有该注解返回 null
        final String methodAccess = getPermValue(targetMethod);

        // 如果该方法上没有权限注解，直接调用目标方法
        if (StringUtils.isBlank(methodAccess)) {
            return joinPoint.proceed();
        }

        // 获取当前用户的 id
        Long id = UserIdThreadLocal.get();

        if (PermCache.userHasPerm(id, methodAccess)) {
            // 包含对应权限则放行
            return joinPoint.proceed();
        } else {
            // 否则直接抛出自定义的权限不足异常，让全局异常处理
            throw new NoPermissionException("权限不足");
        }

    }

    /**
     * 获取指定方法上的 Perm 权限的值
     *
     * @param method 方法
     * @return 若方法上存在 @Perm 注解，获取注解的 value 值，不存在则返回 null
     */
    private static String getPermValue(Method method) {
        // 获取该方法的 Perm
        if (method.isAnnotationPresent(Perm.class)) {
            Perm annotation = method.getAnnotation(Perm.class);
            return annotation.value();
        }
        return null;
    }

}
