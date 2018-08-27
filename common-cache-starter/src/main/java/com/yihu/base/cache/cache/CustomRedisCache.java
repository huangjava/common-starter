package com.yihu.base.cache.cache;

import com.yihu.base.async.AsyncExecutorUtils;
import com.yihu.base.cache.config.CacheKeyGenerator;
import com.yihu.base.cache.support.CacheSupport;
import com.yihu.base.cache.lock.CacheLock;
import com.yihu.base.cache.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheElement;
import org.springframework.data.redis.cache.RedisCacheKey;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.Assert;

public class CustomRedisCache extends RedisCache{

    public Logger logger = LoggerFactory.getLogger(CustomRedisCache.class);

    private Long expireTime;

    private Long refreshTime;

    private RedisOperations redisOperations;

    @Autowired
    private AsyncExecutorUtils asyncExecutorUtils;

    private CacheSupport getCacheSupport() {
        return SpringContextUtils.getBean(CacheSupport.class);
    }

    private byte[] prefix;

    public CustomRedisCache(String name, byte[] prefix, RedisOperations<?, ?> redisOperations, long expiration) {
        super(name, prefix, redisOperations, expiration);
    }

    public CustomRedisCache(String name, byte[] prefix, RedisOperations<? extends Object, ? extends Object> redisOperations, long expiration, long refreshTime) {
        super(name, prefix, redisOperations, expiration);
        this.redisOperations = redisOperations;
        this.refreshTime = refreshTime;
        this.prefix = prefix;
    }

    public CustomRedisCache(String name, byte[] prefix, RedisOperations<? extends Object, ? extends Object> redisOperations, long expiration, long refreshTime, boolean allowNullValues) {
        super(name, prefix, redisOperations, expiration, allowNullValues);
        this.redisOperations = redisOperations;
        this.refreshTime = refreshTime;
        this.prefix = prefix;
    }

    /**
     * 重写get方法，获取到缓存后再次取缓存剩余的时间，如果时间小余我们配置的刷新时间就手动刷新缓存。
     * 为了不影响get的性能，启用后台线程去完成缓存的刷新
     * 并且只开一个线程去刷新数据。
     * @param key
     * @return
     */
    @Override
    public ValueWrapper get(Object key) {
        String cacheKey = CacheKeyGenerator.getCacheKey();

        ValueWrapper valueWrapper = this.get(cacheKey);

        if (null != valueWrapper) {
            // 刷新缓存数据
            refreshCache(key, cacheKey);
        }
        return valueWrapper;
    }

    /**
     * 重写父类的get函数。
     * 父类的get方法，是先使用exists判断key是否存在，不存在返回null，存在再到redis缓存中去取值。这样会导致并发问题，
     * 假如有一个请求调用了exists函数判断key存在，但是在下一时刻这个缓存过期了，或者被删掉了。
     * 这时候再去缓存中获取值的时候返回的就是null了。
     * 可以先获取缓存的值，再去判断key是否存在。
     *
     * @param cacheKey
     * @return
     */
    @Override
    public RedisCacheElement get(final RedisCacheKey cacheKey) {

        Assert.notNull(cacheKey, "CacheKey must not be null!");

        // 根据key获取缓存值
        RedisCacheElement redisCacheElement = new RedisCacheElement(cacheKey, fromStoreValue(lookup(cacheKey)));
        // 判断key是否存在
        Boolean exists = (Boolean) redisOperations.execute(new RedisCallback<Boolean>() {

            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.exists(cacheKey.getKeyBytes());
            }
        });

        if (!exists.booleanValue()) {
            return null;
        }

        return redisCacheElement;
    }

    /**
     * 新缓存数据
     * @param key
     * @param cacheKeyStr
     */
    @Async("asyncExecutorUtils")
    public void refreshCache(Object key, String cacheKeyStr) {
        Long ttl = this.redisOperations.getExpire(cacheKeyStr);
        if (null != ttl && ttl <= getRefreshTime()) {
            // 尽量少的去开启线程，因为线程池是有限的
            // 加一个分布式锁，只放一个请求去刷新缓存

            CacheLock cacheLock = new CacheLock((RedisTemplate) redisOperations, cacheKeyStr + "_lock");
            try {
                if (cacheLock.lock()) {
                    // 获取锁之后再判断一下过期时间，看是否需要加载数据
                    Long ttl2 = CustomRedisCache.this.redisOperations.getExpire(cacheKeyStr);
                    if (null != ttl2 && ttl2 <= getRefreshTime()) {
                        // 通过获取代理方法信息重新加载缓存数据
                        CustomRedisCache.this.getCacheSupport().refreshCacheByKey(CustomRedisCache.super.getName(), key.toString());
                    }
                }
            } catch (Exception e) {
                logger.error("刷新缓存失败");
            } finally {
                cacheLock.unlock();
            }
        }
    }


    public Long getExpireTime() {
        return expireTime;
    }

    public Long getRefreshTime() {
        return refreshTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public void setRefreshTime(Long refreshTime) {
        this.refreshTime = refreshTime;
    }
}
