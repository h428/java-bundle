package com.hao.bundle.demo.common.util;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class MethodUtil {

    public static String getFullMethodName(Method method) {
        Class<?> clazz = method.getDeclaringClass();
        String clazzName = clazz.getName();
        String methodName = method.getName();
        Parameter[] parameters = method.getParameters();
        Class<?>[] parameterTypes = method.getParameterTypes();

        StringBuilder builder = new StringBuilder(clazzName + "." + methodName + "(");

        for (int i = 0; i < parameters.length; i++) {
            if (i != 0) {
                builder.append(", ");
            }
            builder.append(ClassUtil.getPrintTypeName(parameterTypes[i], true))
                .append(" ")
                .append(parameters[i].getName());
        }
        return builder.append(")").toString();
    }

    public static void main(String[] args) {
        Class<CookieUtil> clazz = CookieUtil.class;
        Method method = clazz.getMethods()[0];
        System.out.println(getFullMethodName(method));
    }

}
