package com.yihu.base.cache.support;

import com.yihu.base.cache.manager.CacheManagerFactory;
import com.yihu.base.cache.manager.CustomCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MethodInvoker;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CacheSupportImpl implements CacheSupport {

    @Autowired
    private CacheManagerFactory cacheManagerFactory;
    /**
     * 根据name获取cache列表
     * @param annoationCacheName
     * @return
     */
    public Collection<? extends Cache> getCache(Set<String> annoationCacheName){
        Collection<String> cacheNames = getAnnoationValues(annoationCacheName);
        if(null == cacheNames){
            return Collections.EMPTY_LIST;
        }
        Collection<Cache> cacheResults = new ArrayList<>();
        for(String cacheName:cacheNames){
            Cache cache = cacheManagerFactory.getCacheManager().getCache(cacheName);
            if(null == cache){
                throw new IllegalArgumentException("cannot find cache named "+cacheName);
            }
            cacheResults.add(cache);
        }
        return cacheResults;
    }


    /**
     * 获取注解上的cacheName值
     * @param annoationNames
     * @return
     */
    public Collection<String> getAnnoationValues(Set<String> annoationNames){
        Collection<String> cacheNames = new HashSet<>();
        for(String cacheName:annoationNames){
            String[] cacheParams =  cacheName.split(CacheSupport.SPERATOR);
            //注解的“#”前的第一个为缓存的name
            String realCacheName = cacheParams[0];
            cacheNames.add(realCacheName);
        }
        return  cacheNames;
    }


    /**
     * 调用方法
     * @param cacheInvocation
     * @return
     */
    public Object invoke(CacheInvocation cacheInvocation){
        Object object = null;
        MethodInvoker methodInvoker =  new MethodInvoker();
        methodInvoker.setTargetObject(cacheInvocation.getTargetBean());
        methodInvoker.setTargetMethod(cacheInvocation.getTargetMethod().getName());
        methodInvoker.setArguments(cacheInvocation.getArguments());
        try {
            methodInvoker.prepare();
            object = methodInvoker.invoke();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }


    @Override
    public void registerInvocation(Object invokedBean, Method invokedMethod, Class[] invocationParamTypes, Object[] invocationArgs, Set<String> annoationCacheNames, String cacheKey) {

    }

    @Override
    public void refreshCacheByKey(String cacheName, String cacheKey) {

    }
}
