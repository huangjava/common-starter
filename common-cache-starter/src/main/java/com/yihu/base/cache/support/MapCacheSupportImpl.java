package com.yihu.base.cache.support;

import com.yihu.base.cache.cache.CustomMapCache;
import com.yihu.base.cache.config.CacheKeyGenerator;
import com.yihu.base.cache.manager.CustomCacheManager;
import com.yihu.base.cache.util.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class MapCacheSupportImpl extends CacheSupportImpl {

    private ConcurrentMap<String,Object> invocationMap = new ConcurrentHashMap<>();

    @Autowired
    private CustomCacheManager customCacheManager;

    @Override
    public void registerInvocation(Object invokedBean, Method invokedMethod, Class[] invocationParamTypes, Object[] invocationArgs, Set<String> annoationCacheNames, String cacheKey) {
        Collection<? extends Cache> caches = getCache(annoationCacheNames);
        CacheInvocation cacheInvocation = new CacheInvocation(CacheKeyGenerator.getCacheKey(),invokedBean,invokedMethod,invocationArgs,invocationParamTypes);
        for(Cache cache:caches){
            if(cache instanceof CustomMapCache){
                CustomMapCache customMapCache = (CustomMapCache)cache;
                Long diffTime = System.currentTimeMillis() - customMapCache.getStartTime();
                if(diffTime >= customMapCache.getExpireTime() || diffTime <= customMapCache.getRefreshTime()){
                    invocationMap.put(cacheKey,cacheInvocation);
                }
            }
        }
    }

    @Override
    public void refreshCacheByKey(String cacheName, String cacheKey) {
        CacheInvocation cacheInvocation = (CacheInvocation)invocationMap.get(cacheKey);
        if(null != cacheInvocation){
            refreshCache(cacheInvocation,cacheName);
        }
    }

    private void refreshCache(CacheInvocation cacheInvocation,String cacheName){
        Object computed = invoke(cacheInvocation);
        //获取缓存对象
//        ConcurrentMapCacheManager concurrentMapCacheManager = new ConcurrentMapCacheManager();
        Cache cache = customCacheManager.getCache(cacheName);
        //更新缓存
        cache.put(cacheInvocation.getKey(),computed);

        CustomMapCache customMapCache = (CustomMapCache)cache;
        invocationMap.remove(customMapCache.getName());

        ConcurrentMap<Object, Object> cacheMap = (ConcurrentMap<Object, Object>) ReflectionUtils.getFieldValue(ConcurrentMapCache.class, "store");
        cacheMap.remove(customMapCache.getName());
    }

}
