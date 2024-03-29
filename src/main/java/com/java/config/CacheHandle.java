package com.java.config;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 缓存处理
 */
@Component("cacheHandle")
public class CacheHandle {

    @Resource
    private CacheManager cacheManager;

    private final String USER_KEY = "users";

    /**
     * 获取用户缓存对象
     */
    public Cache getUserCache(){
        return cacheManager.getCache(USER_KEY);
    }

    /**
     * 存储登录用户信息
     * @param key 缓存用户 token
     * @param val 登录用户 信息
     */
    public void addUserCache(String key, Object val) {
        Cache cache = getUserCache();
        cache.put(key, val);
    }

    /**
     * 移除缓存登录用户信息
     * @param key 缓存用户 token
     */
    public void removeUserCache(String key){
        Cache cache = getUserCache();
        cache.evict(key);
    }

    /**
     * 获取缓存的登录用户信息
     */
    public String getUserInfoCache(String key){
        Cache cache = getUserCache();
        return cache.get(key, String.class);
    }
}
