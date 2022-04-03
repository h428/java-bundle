package com.hao.bundle.demo.common.util;

import cn.hutool.core.bean.BeanUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class EnumUtil {

    /**
     * 用于将枚举类转化为对应的 MapList
     * @param clazz 枚举类所属 class
     * @param <T> 泛型参数，即枚举类型
     * @return 枚举对应的 MapList
     */
    public static <T> List<Map<String, Object>> toMapList(Class<T> clazz) {

        try {
            // 获取 jvm 针对枚举类生成的 $VALUES 属性，其为枚举数组
            Field valuesField = clazz.getDeclaredField("$VALUES");
            valuesField.setAccessible(true);
            T[] enumArray = (T[]) valuesField.get(null); // 转化为对应的枚举数组

            List<Map<String, Object>> res = new ArrayList<>(); // 保存结果

            for (T enumInstance : enumArray) {
                Map<String, Object> map = BeanUtil.beanToMap(enumInstance);
                res.add(map);
            }

            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 根据枚举类的指定属性查找枚举的实例
     * @param clazz 枚举类的 Class 对象
     * @param colName 检索的属性名
     * @param colValue 属性值
     * @param <T> 枚举类型泛型
     * @return 返回枚举实例
     */
    private static <T extends Enum<T>> T findByColumn(Class<T> clazz, String colName, Object colValue) {
        try {
            // 获取 jvm 针对枚举类生成的 $VALUES 属性，其为枚举数组
            Field valuesField = clazz.getDeclaredField("$VALUES");
            valuesField.setAccessible(true);
            T[] enumArray = (T[]) valuesField.get(null); // 转化为对应的枚举数组

            for (T enumInstance : enumArray) {
                Map<String, Object> map = BeanUtil.beanToMap(enumInstance);
                if (Objects.equals(map.get(colName), colValue)) {
                    return enumInstance;
                }
            }

            throw new IllegalArgumentException(String.format("枚举值 %s(%s) 不存在", colValue, colValue.getClass().getSimpleName()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static <T extends Enum<T>> T findByCode(Class<T> clazz, Object codeValue) {
        return findByColumn(clazz, "code", codeValue);
    }

    /**
     * 根据枚举类的指定属性查找枚举的实例
     * @param clazz 枚举类的 Class 对象
     * @param col 属性的方法引用
     * @param colValue 属性的值
     * @param <T> 枚举类型
     * @param <R> 属性值类型
     * @return 枚举实例
     */
    public static <T extends Enum<T>, R> T findByColumn(Class<T> clazz, Function<T, R> col, R colValue) {
        try {
            // 获取 jvm 针对枚举类生成的 $VALUES 属性，其为枚举数组
            Field valuesField = clazz.getDeclaredField("$VALUES");
            valuesField.setAccessible(true);
            T[] enumArray = (T[]) valuesField.get(null); // 转化为对应的枚举数组

            for (T enumInstance : enumArray) {
                R apply = col.apply(enumInstance);
                if (Objects.equals(apply, colValue)) {
                    return enumInstance;
                }
            }

            throw new IllegalArgumentException(String.format("枚举值 %s(%s) 不存在", colValue, colValue.getClass().getSimpleName()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
