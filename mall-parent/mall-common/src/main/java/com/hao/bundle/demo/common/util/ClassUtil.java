package com.hao.bundle.demo.common.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ClassUtil {

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static <T> void setValue(Class<T> clazz, T entity, String fieldName, Object value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(entity, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void setValue(T entity, String fieldName, Object value) {

        if (entity == null) {
            return;
        }

        try {
            Class<?> clazz = entity.getClass();
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(entity, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Object getValue(T entity, String fieldName){

        if (entity == null) {
            return null;
        }

        try {
            Class<?> clazz = entity.getClass();
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> objectToMap(Object entity) {

        if (entity == null) {
            return null;
        }

        Map<String, Object> map = new HashMap<>();

        try {
            Class<?> clazz = entity.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();

            for (Field field : declaredFields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(entity));
            }

            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPrintTypeName(Class<?> clazz, boolean simpleName) {

        // 处理数组类型
        String name = clazz.getName();
        if (name.startsWith("[")) {
            // 去除前第一位的 [
            name = name.substring(1);
            // 将数组类型的元素类型替换为对应的类型，主要处理基础类型
            String typeCh = name.charAt(0) + "";
            name = name.replaceAll(typeCh, arrayElementTypeNameMap.get(typeCh));
            // 去除结尾的 ;
            name = name.replace(";", "");
            // 添加上 [] 表示数组类型
            name += "[]";
        }

        if (simpleName) {
            int idx = name.lastIndexOf(".");
            name = name.substring(idx + 1);
        }

        return name;
    }

    public static void main(String[] args) {

        Class<?> clazz = String[].class;
        System.out.println(clazz.getName());
        System.out.println(getPrintTypeName(clazz, true));
    }

    private static final Map<String, String> arrayElementTypeNameMap = new HashMap<>();

    static {
        arrayElementTypeNameMap.put("Z", "boolean");
        arrayElementTypeNameMap.put("B", "byte");
        arrayElementTypeNameMap.put("C", "char");
        arrayElementTypeNameMap.put("S", "short");
        arrayElementTypeNameMap.put("I", "int");
        arrayElementTypeNameMap.put("F", "float");
        arrayElementTypeNameMap.put("J", "long");
        arrayElementTypeNameMap.put("D", "double");
        arrayElementTypeNameMap.put("V", "void");
        arrayElementTypeNameMap.put("L", ""); // 类类型直接替换为空串，使用后面标记的类型
    }

}
