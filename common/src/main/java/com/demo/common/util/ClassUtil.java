package com.demo.common.util;

import java.lang.reflect.Field;

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
        try {
            Class<?> clazz = entity.getClass();
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
