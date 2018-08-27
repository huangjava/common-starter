package com.yihu.base.cache.manager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;

public interface CustomCacheManager extends CacheManager{
     //注解分隔符
    String SEPARATOR = "#";

    // SpEL标示符
    String MARK = "$";

    Long getExpireTime();

    Long getRefreshTime();

    Long getExpireTime(String cacheName,String[] cacheParams);

    Long getRefreshTime(String[] cacheParams);

}

