package com.hao.bundle.todo.common.aspect;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import java.lang.reflect.Method;
import javax.persistence.EntityNotFoundException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 由于 Spring Data JPA 调用 get 会暂时得到代理对象而不会得到 null，而 Service 层内部返回的 DTO 都是通过
 * hutool 的 BeanUtil.copyProperties 将 Entity 转化为 DTO，因此在执行属性拷贝时，会由 Spring 抛出
 * EntityNotFoundException 异常，并被 hutool 捕获并包装，因此需要提供统一的切面逻辑，对异常链上包含
 * EntityNotFoundException 的方法进行处理，返回默认的方法返回值
 */
@Aspect
@Component
public class JpaNoEntityAspect {

    private static final Logger logger = LoggerFactory.getLogger(JpaNoEntityAspect.class);

    /**
     * 切面：拦截 service 下的所有方法
     */
    @Pointcut("execution(public * com.hao.bundle.todo.service.*.*(..))")
    public void servicePointcut() {}

    /**
     * 拦截逻辑
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("servicePointcut()")
    public Object timer(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        try {
            // 一切正常的情况下，继续执行被拦截的方法
            return proceedingJoinPoint.proceed();
        } catch (Throwable e) {

            // 若异常链上找到 EntityNotFoundException 则表示尝试获实体值出错
            Throwable cause = e;
            while (cause != null) {
                if (cause instanceof EntityNotFoundException) {
                    MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
                    Method method = signature.getMethod(); //获取被拦截的方法
                    String className = method.getDeclaringClass().getName(); // 获取方法所在的 service
                    String methodName = method.getName(); //获取被拦截的方法名
                    Object[] args = proceedingJoinPoint.getArgs(); // 获取方法调用时的实参
                    String argsStr = StrUtil.join(", ", args); // 实参转化为："p1, p2, p3" 这样的形式
                    Class<?> returnType = method.getReturnType(); // 获取返回类型
                    Object returnValue = ClassUtil.getDefaultValue(returnType); // 根据返回类型确定默认值
                    logger.info("{}.{}({}) 调用过程获取实体数据失败，对应的实体数据不存在，即将返回默认值 {}",
                        className, methodName, argsStr, returnValue);
                    return returnValue;
                }
                cause = cause.getCause();
            }
            throw e;
        }
    }
}
