package com.yihu.base.cache.util;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class RedisTemplateUtils {

    private static RedisTemplate redisTemplate;

    //对方法进行同步，以确保redisTemplate只初始化一次
    public static synchronized RedisTemplate getRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        if (null == redisTemplate) {
            redisTemplate = new RedisTemplate();
            redisTemplate.setConnectionFactory(redisConnectionFactory);

            JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
            redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
            redisTemplate.setHashValueSerializer(jdkSerializationRedisSerializer);

            //key采用StringRedisSserializer来序列化
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setHashKeySerializer(new StringRedisSerializer());

            redisTemplate.afterPropertiesSet();
        }
        return redisTemplate;
    }
}

