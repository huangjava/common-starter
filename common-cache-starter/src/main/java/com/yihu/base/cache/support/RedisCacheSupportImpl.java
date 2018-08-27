package com.yihu.base.cache.support;

import com.yihu.base.cache.cache.CustomRedisCache;
import com.yihu.base.cache.config.CacheKeyGenerator;
import com.yihu.base.cache.manager.CustomCacheManager;
import com.yihu.base.cache.util.RedisTemplateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCacheSupportImpl extends CacheSupportImpl {

    @Autowired
    private CustomCacheManager customCacheManager;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Override
    public void registerInvocation(Object invokedBean, Method invokedMethod, Class[] invocationParamTypes, Object[] invocationArgs, Set<String> annoationCacheNames, String cacheKey) {
        Collection<? extends Cache> caches = getCache(annoationCacheNames);
        CacheInvocation cacheInvocation = new CacheInvocation(CacheKeyGenerator.getCacheKey(),invokedBean,invokedMethod,invocationArgs,invocationParamTypes);
        for(Cache cache:caches){
            if(cache instanceof CustomRedisCache){
                CustomRedisCache customRedisCache = (CustomRedisCache)cache;
                RedisTemplate redisTemplate = RedisTemplateUtils.getRedisTemplate(redisConnectionFactory);
                redisTemplate.opsForValue().set(CacheKeyGenerator.getCacheKey(),cacheInvocation,customRedisCache.getExpireTime(), TimeUnit.SECONDS);
            }
        }
    }

    @Override
    public void refreshCacheByKey(String cacheName, String cacheKey) {
        RedisTemplate redisTemplate = RedisTemplateUtils.getRedisTemplate(redisConnectionFactory);
        CacheInvocation cacheInvocation = (CacheInvocation)redisTemplate.opsForValue().get(cacheKey);
        if(null != cacheInvocation){
            refreshCache(cacheName,cacheInvocation);
        }
    }

    public void refreshCache(String cacheName,CacheInvocation cacheInvocation){
        Object computed = invoke(cacheInvocation);
        //获取缓存对象
        Cache cache = customCacheManager.getCache(cacheName);
        //更新缓存
        cache.put(cacheInvocation.getKey(),computed);

        RedisTemplate redisTemplate = RedisTemplateUtils.getRedisTemplate(redisConnectionFactory);
        CustomRedisCache customRedisCache = (CustomRedisCache)cache;
        redisTemplate.expire(cacheInvocation.getKey(),customRedisCache.getExpireTime(),TimeUnit.SECONDS);
    }

}
