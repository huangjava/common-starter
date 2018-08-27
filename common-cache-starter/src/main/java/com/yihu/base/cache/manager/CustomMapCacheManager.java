package com.yihu.base.cache.manager;

import com.yihu.base.cache.cache.CustomMapCache;
import com.yihu.base.cache.util.ReflectionUtils;
import com.yihu.base.cache.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class CustomMapCacheManager extends ConcurrentMapCacheManager implements CustomCacheManager{

    @Value("expire-time")
    private Long expireTime;

    @Value("refresh-time")
    private Long refreshTime;

    private ConcurrentMapCacheManager concurrentMapCacheManager;

    @Autowired
    private DefaultListableBeanFactory beanFactory;

    private static final String SUPER_CACHEMAP = "cacheMap";

    public ConcurrentMapCacheManager getInstance(){
        if(null == concurrentMapCacheManager){
            concurrentMapCacheManager = SpringContextUtils.getBean(ConcurrentMapCacheManager.class);
        }
        return concurrentMapCacheManager;
    }

    @Override
    public Long getExpireTime() {
        return null;
    }

    @Override
    public Long getRefreshTime() {
        return null;
    }

    /**
     * 获取过期时间
     * @return
     */
    @Override
    public Long getExpireTime(String cacheName, String[] cacheParams) {
        // 有效时间，初始化获取默认的有效时间
        Long expirationSecondTime = null;

        // 设置key有效时间
        if (cacheParams.length > 1) {
            String expirationStr = cacheParams[1];
            if (!StringUtils.isEmpty(expirationStr)) {
                // 支持配置过期时间使用EL表达式读取配置文件时间
                if (expirationStr.contains(MARK)) {
                    expirationStr = beanFactory.resolveEmbeddedValue(expirationStr);
                }
                expirationSecondTime = Long.parseLong(expirationStr);
            }
        }
        if(null == expirationSecondTime){
            expirationSecondTime = this.getExpireTime();
        }
        return expirationSecondTime;
    }

    /**
     * 获取自动刷新时间
     * @return
     */
    @Override
    public Long getRefreshTime(String[] cacheParams) {
        // 自动刷新时间，默认是0
        Long refreshTime = 0L;
        // 设置自动刷新时间
        if (cacheParams.length > 2) {
            String refreshTimeStr = cacheParams[2];
            if (!StringUtils.isEmpty(refreshTimeStr)) {
                // 支持配置刷新时间使用EL表达式读取配置文件时间
                if (refreshTimeStr.contains(MARK)) {
                    refreshTimeStr = beanFactory.resolveEmbeddedValue(refreshTimeStr);
                }
                refreshTime = Long.parseLong(refreshTimeStr);
            }
        }
        if(null == refreshTime){
            refreshTime = this.getRefreshTime();
        }
        return refreshTime;
    }

    @Override
    public Cache getCache(String name){
        String[] cacheParams = name.split(CustomCacheManager.SEPARATOR);
        String cacheName = cacheParams[0];
        if(StringUtils.isEmpty(cacheName)){
            return null;
        }
        //注解里面的过期时间覆盖默认的过期时间
        long expireTime = getExpireTime(name,cacheParams);

        //注解里面的刷新时间
        long refreshTime = getRefreshTime(cacheParams);

        Object obj =  ReflectionUtils.getFieldValue(getInstance(),SUPER_CACHEMAP);
        if(null != obj && obj instanceof ConcurrentHashMap){
            ConcurrentHashMap<String,Cache> cacheMap = (ConcurrentHashMap<String,Cache>)obj;
            return getCache(name,expireTime,refreshTime,cacheMap);
        }else{
            return super.getCache(name);
        }
    }

    /**
     * @param cacheName
     * @param expireTime
     * @param refreshTime
     * @param cacheMap
     * @return
     */
    public Cache getCache(String cacheName, long expireTime, long refreshTime, ConcurrentHashMap<String,Cache> cacheMap){
        Cache cache = cacheMap.get(cacheName);
        if(null != cache){
            return cache;
        }else {
            synchronized (cacheMap){
                cache = cacheMap.get(cacheName);
                if(null == cache){
                    cache = createConcurrentMapCache(cacheName,expireTime,refreshTime);
                    cacheMap.put(cacheName,cache);
                }
            }
        }
        return cache;
    }

    public CustomMapCache createConcurrentMapCache(String cacheName, long expirationTime, long refreshTime) {
        return new CustomMapCache(cacheName,expirationTime,refreshTime);
    }

}
