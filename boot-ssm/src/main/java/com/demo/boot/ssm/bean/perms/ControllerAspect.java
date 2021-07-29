package com.demo.boot.ssm.bean.perms;

import com.demo.base.component.exception.NoPermissionException;
import com.demo.base.component.mapper2.AddUserMapper;
import com.demo.boot.ssm.bean.thredlocal.UserIdThreadLocal;
import java.lang.reflect.Method;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

@Aspect
@Component
public class ControllerAspect {


    @Autowired
    private AddUserMapper addUserMapper;


    /**
     * 定义切点
     */
    @Pointcut("execution(public * com.demo.boot.ssm.controller.*.*(..))")
    public void privilege() {

    }

    /**
     * 权限环绕通知
     */
    @ResponseBody
    @Around("privilege()")
    public Object isAccessMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取访问目标方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        //得到方法注解上的的访问权限
        final String methodAccess = AnnotationParse.privilegeParse(targetMethod);

        //如果该方法上没有权限注解，直接调用目标方法
        if (StringUtils.isBlank(methodAccess)) {
            return joinPoint.proceed();
        } else {
            // 获取当前用户的 id
            Long id = UserIdThreadLocal.get();

            // todo cache
            // 调用 mapper 查询当前用户是否具有方法上声明的权限
            Set<String> permissionSet = this.addUserMapper.queryStringPermSetByUserId(id, null);

            if (permissionSet.contains(methodAccess)) {
                // 包含对应权限则放行
                return joinPoint.proceed();
            } else {
                // 否则直接抛出自定义的权限不足异常，让全局异常处理
                throw new NoPermissionException("权限不足");
            }
        }
    }


}
