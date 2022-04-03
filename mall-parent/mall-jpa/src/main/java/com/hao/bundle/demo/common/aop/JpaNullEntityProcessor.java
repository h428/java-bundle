package com.hao.bundle.demo.common.aop;

import com.hao.bundle.demo.common.util.MethodUtil;
import java.lang.reflect.Method;
import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 处理 Jpa 默认的 getOne(id) 方法找不到实体而抛出 javax.persistence.EntityNotFoundException 异常的问题；
 * 为了统一和 Mybatis 工程的行为，拦截 service 层方法，若捕获到对应的异常，直接返回 null；
 * 当然也可以直接在各个 service 内部使用 findOne 配合 Optional 手动处理；
 */
@Aspect
@Component
@Slf4j
public class JpaNullEntityProcessor {

    /**
     * 定义切面：匹配 com.hao.bundle.demo.service 及子包下的所有方法
     */
    @Pointcut("execution(* com.hao.bundle.demo.service..*.*(..))")
    public void aspect() {

    }

    @Around("aspect()")
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String fullMethodName = MethodUtil.getFullMethodName(method);

        try {
            // 正常调用 service 方法
            return joinPoint.proceed();
        } catch (EntityNotFoundException e) {
            // 若捕捉到 javax.persistence.EntityNotFoundException 异常则直接返回 null
            log.info("方法 {} 调用过程由 jpa 抛出 EntityNotFoundException 异常，将直接返回 null", fullMethodName);
            return null;
        }
    }



}
