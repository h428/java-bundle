package com.demo.common.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Type;
import java.util.*;

public class FastJsonUtil {

    // obj -> json
    public static <T> String getJson(T obj) {
        return JSONObject.toJSONString(obj); // 实际上调用的是父类的 JSON.toJSONString(bean);
    }

    // bean 或嵌套的 bean，内部不允许出现集合类型
    public static <T> T getBean(String json, Class<T> clazz) {
        return JSONObject.parseObject(json, clazz);
    }


    // 单层 List
    public static <T> List<T> getList(String json, Class<T> entityType) {
        return JSONObject.parseObject(json, new TypeReference<List<T>>(entityType){});
    }

    // 单层 Set
    public static <T> Set<T> getSet(String json, Class<T> entityType) {
        return JSONObject.parseObject(json, new TypeReference<Set<T>>(entityType){});
    }

    // 单层 Map
    public static <K, V> Map<K, V> getMap(String json, Class<K> keyType, Class<V> valueType) {
        return JSONObject.parseObject(json, new TypeReference<Map<K, V>>(keyType, valueType){});
    }

    public static <K, T> Map<K, List<T>> getMapList(String json, Class<K> keyType, Class<T> entityType) {
        return JSONObject.parseObject(json, new TypeReference<Map<K, List<T>>>(keyType, entityType){});
    }

    // 互相嵌套，逐级声明泛型，
    public static <T> Set<List<T>> getSetListExample(String json, Class<T> clazz) {
//        return JSONObject.parseObject(json, new TypeReference<Set<List<T>>>(clazz){}); // 直接使用 TypeReference 写死
        return JSONObject.parseObject(json, buildType(Set.class, List.class, clazz)); // 更加通用的方法，可以无限嵌套
    }


    private static Type buildType(Type... types) {
        ParameterizedTypeImpl beforeType = null;
        if (types != null && types.length > 0) {
            for (int i = types.length - 1; i > 0; i--) {
                beforeType = ParameterizedTypeImpl.make((Class<?>) types[i - 1],new Type[]{beforeType == null ? types[i] : beforeType}, types[i - 1]);
//                beforeType = new ParameterizedTypeImpl(new Type[]{beforeType == null ? types[i] : beforeType}, null, types[i - 1]);
            }
        }
        return beforeType;
    }




    public static void main(String[] args) {



        Map<String, List<User>> map = new HashMap<>();
        for (int i = 0; i < 5; ++i) {
            List<User> list = new ArrayList<>();
            for (int j = 0; j < 10; ++j) {
                list.add(EntityUtil.generateRandomOne(User.class));
            }
            map.put(""+i, list);
        }

        String json = getJson(map);
        System.out.println(json);


        Map<String, List<User>> mapList = getMapList(json, String.class, User.class);

        System.out.println(mapList);
    }


    public static class User{
        private Long id;
        private String userName;
        private Integer age;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }



}
