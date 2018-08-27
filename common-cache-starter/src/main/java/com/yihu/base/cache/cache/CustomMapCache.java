package com.yihu.base.cache.cache;

import com.yihu.base.cache.config.CacheKeyGenerator;
import com.yihu.base.cache.support.CacheSupport;
import com.yihu.base.cache.util.ReflectionUtils;
import com.yihu.base.cache.util.SpringContextUtils;
import org.springframework.cache.concurrent.ConcurrentMapCache;

import java.util.concurrent.ConcurrentMap;

public class CustomMapCache extends ConcurrentMapCache{

    private Long expireTime;

    private Long refreshTime;

    private Long startTime;

    public Long getStartTime() {
        return startTime;
    }

    public CustomMapCache(String name) {
        super(name);
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public Long getRefreshTime() {
        return refreshTime;
    }

    public CustomMapCache(String name,Long expireTime, Long refreshTime){
        super(name);
        this.expireTime = expireTime;
        this.refreshTime = refreshTime;
        this.startTime = System.currentTimeMillis();
    }


    private CacheSupport getCacheSupport() {
        return SpringContextUtils.getBean(CacheSupport.class);
    }

    /**
     * 重写get方法，获取到缓存后再次取缓存剩余的时间，如果时间小余我们配置的刷新时间就手动刷新缓存,对于内存缓存，直接移除
     * @param key
     * @return
     */
    @Override
    public ValueWrapper get(Object key) {

        String cacheKey = CacheKeyGenerator.getCacheKey();

        ValueWrapper valueWrapper = this.get(key);

        if (null != valueWrapper) {
            // 刷新缓存数据
            refreshCache(key,cacheKey);
        }
        return valueWrapper;
    }

    /**
     * 刷新缓存数据
     */
    private void refreshCache(Object key, String cacheKeyStr) {
        ConcurrentMap<Object, Object> cacheMap = (ConcurrentMap<Object, Object>) ReflectionUtils.getFieldValue(ConcurrentMapCache.class, "store");
        CustomMapCache customMapCache = (CustomMapCache) cacheMap.get(key);
        Long diffTime = System.currentTimeMillis() - customMapCache.getStartTime();
        if (diffTime >= customMapCache.getExpireTime() || diffTime <= customMapCache.getRefreshTime()) {
            synchronized (cacheMap){
                if (diffTime <= customMapCache.getRefreshTime()) {
                    // 通过获取代理方法信息重新加载缓存数据
                    CustomMapCache.this.getCacheSupport().refreshCacheByKey(customMapCache.getName(), key.toString());
                }
            }
        }
    }
}
