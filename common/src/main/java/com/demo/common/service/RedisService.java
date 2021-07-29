package com.demo.common.service;

import com.demo.common.util.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private static Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    public StringRedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 写入redis缓存（不设置expire存活时间）
     *
     * @param key   键
     * @param value 值
     * @return 成功 true
     */
    public boolean setStr(final String key, String value) {
        boolean result = false;
        try {
            ValueOperations operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            logger.error("写入redis缓存失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * 写入redis缓存（并设置expire存活时间）
     *
     * @param key    键
     * @param value  值
     * @param expire 存活时间
     * @return
     */
    public boolean setStr(final String key, String value, Long expire) {
        boolean result = false;
        try {
            ValueOperations operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            logger.error("写入redis缓存（设置expire存活时间）失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * 写入redis缓存（不设置expire存活时间）
     *
     * @param key   键
     * @param bean 值
     * @return 成功 true
     */
    public <T> boolean setBean(final String key, T bean) {
        String json = JacksonUtil.toJson(bean);
        return this.setStr(key, json);
    }

    /**
     * 写入redis缓存（并设置expire存活时间）
     *
     * @param key    键
     * @param bean  值
     * @param expire 存活时间
     * @return
     */
    public <T> boolean setBean(final String key, T bean, Long expire) {
        String json = JacksonUtil.toJson(bean);
        return this.setStr(key, json, expire);
    }

    /**
     * 读取redis缓存
     *
     * @param key 键
     * @return 值，不存在则返回 null
     */
    public String getStr(final String key) {
        try {
            ValueOperations operations = redisTemplate.opsForValue();
            Object result = operations.get(key);
            if (result != null) {
                return (String) result;
            }
        } catch (Exception e) {
            logger.error("读取redis缓存失败！错误信息为：" + e.getMessage());
        }
        return null;
    }

    /**
     * 读取redis缓存
     *
     * @param key 键
     * @return 值，不存在则返回 null
     */
    public <T> T getBean(final String key, Class<T> clazz) {
        String value = this.getStr(key);
        return JacksonUtil.fromJsonToBean(value, clazz); // 如果 value 为 null 则转换为也为 null
    }

    public <K, V> Map<K, V> getMap(final String key, Class<K> keyType, Class<V> valueType){
        String value = this.getStr(key);
        return JacksonUtil.fromJsonToMap(value, keyType, valueType);
    }

    public <T> Set<T> getSet(final String key, Class<T> entityType){
        String value = this.getStr(key);
        return JacksonUtil.fromJsonToSet(value, entityType);
    }

    public <T> List<T> getList(final String key, Class<T> entityType){
        String value = this.getStr(key);
        return JacksonUtil.fromJsonToList(value, entityType);
    }

    public <K, T> Map<K, List<T>> getMapList(final String key, Class<K> keyType, Class<T> entityType) {
        String value = this.getStr(key);
        return JacksonUtil.fromJsonToMapList(value, keyType, entityType);
    }


    /**
     * 判断redis缓存中是否有对应的key
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        boolean result = false;
        try {
            result = redisTemplate.hasKey(key);
        } catch (Exception e) {
            logger.error("判断redis缓存中是否有对应的key失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * redis根据key删除对应的value
     *
     * @param key 键
     * @return 成功返回 true
     */
    public boolean remove(final String key) {
        boolean result = false;
        try {
            if (exists(key)) {
                redisTemplate.delete(key);
            }
            result = true;
        } catch (Exception e) {
            logger.error("redis根据key删除对应的value失败！错误信息为：" + e.getMessage());
        }
        return result;
    }

    /**
     * redis根据keys批量删除对应的value
     *
     * @param keys 键的列表
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }
}

