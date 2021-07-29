package com.demo.boot.ssm.bean.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class EhCacheUtil {

    private static CacheManager cacheManager;
    private static Cache cache;

    @Autowired
    public void setCacheManager(CacheManager cacheManager) {
        EhCacheUtil.cacheManager = cacheManager;
        cache = cacheManager.getCache("userCache");
    }


    public static void put(String key, String value) {
        cache.put(key, value);
    }

    public static String get(String key) {
        return cache.get(key, String.class);
    }
}
