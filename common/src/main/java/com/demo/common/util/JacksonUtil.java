package com.demo.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.*;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class JacksonUtil {

    private static Logger logger = LoggerFactory.getLogger(JacksonUtil.class);

    private JacksonUtil() {

    }

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> String toJson(T bean) {
        try {
            return objectMapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            logger.error("将 bean 转化为 json 时发生异常 : ", e);
            return null;
        }
    }


    public static <T> T fromJsonToBean(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            logger.error("从 json 反序列化为 bean 时发生异常 :", e);
            return null;
        }
    }

    // 使用 getTypeFactory
    public static <T> List<T> fromJsonToList(String json, Class<T> entityClass) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, entityClass);
            return objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            logger.error("从 json 反序列化为 list 时发生异常 :", e);
            return null;
        }
    }

    // 使用 TypeReference
    public static <T> Set<T> fromJsonToSet(String json, Class<T> entityClass) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Set.class, entityClass);
            return objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            logger.error("从 json 反序列化为 set 时发生异常 :", e);
            return null;
        }
    }

    public static <K, V> Map<K, V> fromJsonToMap(String json, Class<K> keyType, Class<V> entityClass) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(HashMap.class, keyType, entityClass);
            return objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            logger.error("从 json 反序列化为 map 时发生异常 :", e);
            return null;
        }
    }

    public static <K, T> Map<K, List<T>> fromJsonToMapList(String json, Class<K> keyClass, Class<T> entityClass) {
        try {
            JavaType keyType = objectMapper.getTypeFactory().constructType(keyClass);
            JavaType valueType = objectMapper.getTypeFactory().constructParametricType(List.class, entityClass);
            MapType mapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, keyType, valueType);
            return objectMapper.readValue(json, mapType);
        } catch (IOException e) {
            logger.error("从 json 反序列化为 mapList 时发生异常 :", e);
            return null;
        }
    }

    @SuppressWarnings({"rawtypes" })
    public static <T> T fromJsonWithTypeReference(String json, TypeReference typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            logger.error("从 json 通过 typeReference 反序列时发生异常 :", e);
            return null;
        }
    }


    @Data
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    private static class User {
        private String username;
        private String password;
    }

    public static void main(String[] args) {

        testNull();

    }


    private static void testTypeReference() {

        ArrayList<User> list1 = Lists.newArrayList(
                EntityUtil.generateRandomOne(User.class),
                EntityUtil.generateRandomOne(User.class),
                EntityUtil.generateRandomOne(User.class)
        );

        ArrayList<User> list2 = Lists.newArrayList(
                EntityUtil.generateRandomOne(User.class),
                EntityUtil.generateRandomOne(User.class),
                EntityUtil.generateRandomOne(User.class),
                EntityUtil.generateRandomOne(User.class),
                EntityUtil.generateRandomOne(User.class)
        );

        ArrayList<User> list3 = Lists.newArrayList(
                EntityUtil.generateRandomOne(User.class),
                EntityUtil.generateRandomOne(User.class),
                EntityUtil.generateRandomOne(User.class),
                EntityUtil.generateRandomOne(User.class),
                EntityUtil.generateRandomOne(User.class)
        );


        HashMap<String, ArrayList<User>> map = Maps.newHashMap();

        map.put("list1", list1);
        map.put("list2", list2);
        map.put("list3", list3);


        String json = toJson(map);


        TypeReference<HashMap<String, ArrayList<User>>> typeReference =
                new TypeReference<HashMap<String, ArrayList<User>>>() {};

        Map<String, ArrayList<User>> res = fromJsonWithTypeReference(json, typeReference);

        EntityUtil.printString(res);


    }

    private static void testNull() {

        List<String> stringList = Lists.newArrayList();

        stringList.add("2012BJF4");
        stringList.add(null);
        stringList.add("2012BJF5");

        String s = JacksonUtil.toJson(stringList);
        System.out.println(s);

    }

    private static void testBean() {

        User bean = EntityUtil.generateRandomOne(User.class);

        System.out.println(toJson(bean));


        String jsonBean = "{\"id\":63,\"email\":\"email63\",\"userName\":\"userName63\",\"age\":63,\"height\":63.0,\"profile\":\"profile63\",\"status\":63,\"registerTime\":1571925651080,\"lastUpdateTime\":1571925651080,\"loginTime\":1571925651080,\"deleted\":null}";

        User user = fromJsonToBean(jsonBean, User.class);

        EntityUtil.printString(user);

    }

    private static void testList() {
        String jsonList = "[{\"id\":62,\"email\":\"email62\",\"userName\":\"userName62\",\"age\":62,\"height\":62.0,\"profile\":\"profile62\",\"status\":62,\"registerTime\":1571924549708,\"lastUpdateTime\":1571924549708,\"loginTime\":1571924549708,\"deleted\":null},{\"id\":5,\"email\":\"email5\",\"userName\":\"userName5\",\"age\":5,\"height\":5.0,\"profile\":\"profile5\",\"status\":5,\"registerTime\":1571924549708,\"lastUpdateTime\":1571924549708,\"loginTime\":1571924549708,\"deleted\":null},{\"id\":94,\"email\":\"email94\",\"userName\":\"userName94\",\"age\":94,\"height\":94.0,\"profile\":\"profile94\",\"status\":94,\"registerTime\":1571924549708,\"lastUpdateTime\":1571924549708,\"loginTime\":1571924549708,\"deleted\":null},{\"id\":27,\"email\":\"email27\",\"userName\":\"userName27\",\"age\":27,\"height\":27.0,\"profile\":\"profile27\",\"status\":27,\"registerTime\":1571924549708,\"lastUpdateTime\":1571924549708,\"loginTime\":1571924549708,\"deleted\":null},{\"id\":87,\"email\":\"email87\",\"userName\":\"userName87\",\"age\":87,\"height\":87.0,\"profile\":\"profile87\",\"status\":87,\"registerTime\":1571924549708,\"lastUpdateTime\":1571924549708,\"loginTime\":1571924549708,\"deleted\":null}]";
        List<User> userList = fromJsonToList(jsonList, User.class);
        EntityUtil.printString(userList);
    }

    private static void testSet() {
        String jsonList = "[{\"id\":62,\"email\":\"email62\",\"userName\":\"userName62\",\"age\":62,\"height\":62.0,\"profile\":\"profile62\",\"status\":62,\"registerTime\":1571924549708,\"lastUpdateTime\":1571924549708,\"loginTime\":1571924549708,\"deleted\":null},{\"id\":5,\"email\":\"email5\",\"userName\":\"userName5\",\"age\":5,\"height\":5.0,\"profile\":\"profile5\",\"status\":5,\"registerTime\":1571924549708,\"lastUpdateTime\":1571924549708,\"loginTime\":1571924549708,\"deleted\":null},{\"id\":94,\"email\":\"email94\",\"userName\":\"userName94\",\"age\":94,\"height\":94.0,\"profile\":\"profile94\",\"status\":94,\"registerTime\":1571924549708,\"lastUpdateTime\":1571924549708,\"loginTime\":1571924549708,\"deleted\":null},{\"id\":27,\"email\":\"email27\",\"userName\":\"userName27\",\"age\":27,\"height\":27.0,\"profile\":\"profile27\",\"status\":27,\"registerTime\":1571924549708,\"lastUpdateTime\":1571924549708,\"loginTime\":1571924549708,\"deleted\":null},{\"id\":87,\"email\":\"email87\",\"userName\":\"userName87\",\"age\":87,\"height\":87.0,\"profile\":\"profile87\",\"status\":87,\"registerTime\":1571924549708,\"lastUpdateTime\":1571924549708,\"loginTime\":1571924549708,\"deleted\":null}]";
        Set<User> userSet = fromJsonToSet(jsonList, User.class);
        EntityUtil.printString(userSet);
    }

    private static void testMap() {
        String jsonMap = "{\"0\":{\"id\":13,\"email\":\"email13\",\"userName\":\"userName13\",\"age\":13,\"height\":13.0,\"profile\":\"profile13\",\"status\":13,\"registerTime\":1571925803213,\"lastUpdateTime\":1571925803213,\"loginTime\":1571925803213,\"deleted\":null},\"1\":{\"id\":23,\"email\":\"email23\",\"userName\":\"userName23\",\"age\":23,\"height\":23.0,\"profile\":\"profile23\",\"status\":23,\"registerTime\":1571925803213,\"lastUpdateTime\":1571925803213,\"loginTime\":1571925803213,\"deleted\":null},\"2\":{\"id\":33,\"email\":\"email33\",\"userName\":\"userName33\",\"age\":33,\"height\":33.0,\"profile\":\"profile33\",\"status\":33,\"registerTime\":1571925803213,\"lastUpdateTime\":1571925803213,\"loginTime\":1571925803213,\"deleted\":null},\"3\":{\"id\":34,\"email\":\"email34\",\"userName\":\"userName34\",\"age\":34,\"height\":34.0,\"profile\":\"profile34\",\"status\":34,\"registerTime\":1571925803213,\"lastUpdateTime\":1571925803213,\"loginTime\":1571925803213,\"deleted\":null},\"4\":{\"id\":31,\"email\":\"email31\",\"userName\":\"userName31\",\"age\":31,\"height\":31.0,\"profile\":\"profile31\",\"status\":31,\"registerTime\":1571925803213,\"lastUpdateTime\":1571925803213,\"loginTime\":1571925803213,\"deleted\":null}}";
        Map<Integer, User> userMap = fromJsonToMap(jsonMap, Integer.class, User.class);
        EntityUtil.printString(userMap);
    }

    private static void testMapList() {
        String jsonMapList = "{\"0\":[{\"id\":19,\"email\":\"email19\",\"userName\":\"userName19\",\"age\":19,\"height\":19.0,\"profile\":\"profile19\",\"status\":19,\"registerTime\":1571934360103,\"lastUpdateTime\":1571934360103,\"loginTime\":1571934360103,\"deleted\":null},{\"id\":70,\"email\":\"email70\",\"userName\":\"userName70\",\"age\":70,\"height\":70.0,\"profile\":\"profile70\",\"status\":70,\"registerTime\":1571934360103,\"lastUpdateTime\":1571934360103,\"loginTime\":1571934360103,\"deleted\":null},{\"id\":93,\"email\":\"email93\",\"userName\":\"userName93\",\"age\":93,\"height\":93.0,\"profile\":\"profile93\",\"status\":93,\"registerTime\":1571934360103,\"lastUpdateTime\":1571934360103,\"loginTime\":1571934360103,\"deleted\":null}],\"1\":[{\"id\":14,\"email\":\"email14\",\"userName\":\"userName14\",\"age\":14,\"height\":14.0,\"profile\":\"profile14\",\"status\":14,\"registerTime\":1571934360103,\"lastUpdateTime\":1571934360103,\"loginTime\":1571934360103,\"deleted\":null},{\"id\":85,\"email\":\"email85\",\"userName\":\"userName85\",\"age\":85,\"height\":85.0,\"profile\":\"profile85\",\"status\":85,\"registerTime\":1571934360103,\"lastUpdateTime\":1571934360103,\"loginTime\":1571934360103,\"deleted\":null},{\"id\":47,\"email\":\"email47\",\"userName\":\"userName47\",\"age\":47,\"height\":47.0,\"profile\":\"profile47\",\"status\":47,\"registerTime\":1571934360104,\"lastUpdateTime\":1571934360104,\"loginTime\":1571934360104,\"deleted\":null}],\"2\":[{\"id\":77,\"email\":\"email77\",\"userName\":\"userName77\",\"age\":77,\"height\":77.0,\"profile\":\"profile77\",\"status\":77,\"registerTime\":1571934360104,\"lastUpdateTime\":1571934360104,\"loginTime\":1571934360104,\"deleted\":null},{\"id\":63,\"email\":\"email63\",\"userName\":\"userName63\",\"age\":63,\"height\":63.0,\"profile\":\"profile63\",\"status\":63,\"registerTime\":1571934360104,\"lastUpdateTime\":1571934360104,\"loginTime\":1571934360104,\"deleted\":null},{\"id\":86,\"email\":\"email86\",\"userName\":\"userName86\",\"age\":86,\"height\":86.0,\"profile\":\"profile86\",\"status\":86,\"registerTime\":1571934360104,\"lastUpdateTime\":1571934360104,\"loginTime\":1571934360104,\"deleted\":null}],\"3\":[{\"id\":89,\"email\":\"email89\",\"userName\":\"userName89\",\"age\":89,\"height\":89.0,\"profile\":\"profile89\",\"status\":89,\"registerTime\":1571934360104,\"lastUpdateTime\":1571934360104,\"loginTime\":1571934360104,\"deleted\":null},{\"id\":13,\"email\":\"email13\",\"userName\":\"userName13\",\"age\":13,\"height\":13.0,\"profile\":\"profile13\",\"status\":13,\"registerTime\":1571934360104,\"lastUpdateTime\":1571934360104,\"loginTime\":1571934360104,\"deleted\":null},{\"id\":0,\"email\":\"email0\",\"userName\":\"userName0\",\"age\":0,\"height\":0.0,\"profile\":\"profile0\",\"status\":0,\"registerTime\":1571934360104,\"lastUpdateTime\":1571934360104,\"loginTime\":1571934360104,\"deleted\":null}],\"4\":[{\"id\":87,\"email\":\"email87\",\"userName\":\"userName87\",\"age\":87,\"height\":87.0,\"profile\":\"profile87\",\"status\":87,\"registerTime\":1571934360104,\"lastUpdateTime\":1571934360104,\"loginTime\":1571934360104,\"deleted\":null},{\"id\":45,\"email\":\"email45\",\"userName\":\"userName45\",\"age\":45,\"height\":45.0,\"profile\":\"profile45\",\"status\":45,\"registerTime\":1571934360104,\"lastUpdateTime\":1571934360104,\"loginTime\":1571934360104,\"deleted\":null},{\"id\":3,\"email\":\"email3\",\"userName\":\"userName3\",\"age\":3,\"height\":3.0,\"profile\":\"profile3\",\"status\":3,\"registerTime\":1571934360104,\"lastUpdateTime\":1571934360104,\"loginTime\":1571934360104,\"deleted\":null}]}";
        Map<String, List<User>> userMapList = fromJsonToMapList(jsonMapList, String.class, User.class);
        for (Map.Entry<String, List<User>> entry: userMapList.entrySet()) {
            System.out.println(entry.getKey());
            EntityUtil.printString(entry.getValue());
            System.out.println();
        }
    }


}
