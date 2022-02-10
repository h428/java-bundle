package com.hao.bundle.demo.common.util;

import cn.hutool.core.bean.BeanUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
}
