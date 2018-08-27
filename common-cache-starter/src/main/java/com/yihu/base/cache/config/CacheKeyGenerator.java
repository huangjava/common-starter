package com.yihu.base.cache.config;

/**
 * 生成缓存的key
 */
public class CacheKeyGenerator {
    private static String cacheKey;

    public static void setCacheKey(String moduleName,String saasId,String bussinessModuleName) {
        StringBuilder str = new StringBuilder();
        str.append(moduleName + "-");
        str.append(saasId + "-");
        str.append(bussinessModuleName);
        cacheKey = str.toString();
    }

    public static String getCacheKey(){
        return cacheKey;
    }

}
