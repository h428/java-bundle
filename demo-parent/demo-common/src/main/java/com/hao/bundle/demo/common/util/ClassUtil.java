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

}
