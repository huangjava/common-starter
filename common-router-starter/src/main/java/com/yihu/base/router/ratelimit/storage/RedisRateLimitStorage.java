package com.yihu.base.router.ratelimit.storage;

import com.alibaba.fastjson.JSON;
import com.yihu.base.router.model.RateLimitCount;
import com.yihu.base.router.model.Ratelimit;
import com.yihu.base.router.model.RuleRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by chenweida on 2018/4/27 0027.
 */
public class RedisRateLimitStorage extends AbstractRateLimitStorage {

    private Logger logger = LoggerFactory.getLogger(RedisRateLimitStorage.class);
    private static String redisPre = "router:rateLimit:";
    private static String redisPreCount = "router:rateLimitCount:";
    private static String and = ":";

    private StringRedisTemplate redisTemplate;

    @Override
    public Boolean saveRateLimit(Ratelimit ratelimit) {
        try {
            if (StringUtils.isEmpty(ratelimit.getId())) {
                logger.error("Ratelimit id is null");
                return false;
            }
            String key = id(ratelimit.getId());
            Object value = redisTemplate.opsForValue().get(key.toString());
            if (!StringUtils.isEmpty(value)) {
                logger.error("Ratelimit id is exists ");
                return false;
            }

            redisTemplate.opsForValue().set(key.toString(), JSON.toJSON(ratelimit).toString());
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean updateRateLimit(Ratelimit ratelimit) {
        try {
            if (StringUtils.isEmpty(ratelimit.getId())) {
                logger.error("Ratelimit id is null");
                return false;
            }
            String key = id(ratelimit.getId());

            redisTemplate.opsForValue().set(key.toString(), JSON.toJSON(ratelimit).toString());
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean deleteRateLimitById(String id) {
        try {
            redisTemplate.delete(id(id));
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public List<Ratelimit> findAllRatelimit() {
        Set<String> keys = redisTemplate.keys(redisPre + "*");
        List<String> values = redisTemplate.opsForValue().multiGet(keys);
        List<Ratelimit> ratelimits = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            ratelimits.add(JSON.parseObject(values.get(i), Ratelimit.class));
        }
        return ratelimits;
    }

    @Override
    public Ratelimit findRateLimitById(String id) {
        String json = redisTemplate.opsForValue().get(id(id));
        return JSON.parseObject(json, Ratelimit.class);
    }

    @Override
    public Boolean countDec(String id, String requestIP) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String key = redisPreCount + id + and + requestIP;
            if (redisTemplate.hasKey(key)) {
                redisTemplate.watch(key);
                String value = redisTemplate.opsForValue().get(key);
                RateLimitCount rateLimitCount = JSON.parseObject(value, RateLimitCount.class);
                logger.info("当前剩余访问数目：" + rateLimitCount.getCount());
                if (rateLimitCount.getCount() > 0) {
                    SessionCallback<Boolean> sessionCallback = new SessionCallback<Boolean>() {
                        @Override
                        public Boolean execute(RedisOperations operations) throws DataAccessException {
                            //开启事务
                            operations.multi();
                            rateLimitCount.setCount(rateLimitCount.getCount() - 1);
                            redisTemplate.opsForValue().set(key, JSON.toJSONString(rateLimitCount));
                            try {
                                Date endTime = simpleDateFormat.parse(rateLimitCount.getEndTime());
                                if (new Date().after(endTime)) {
                                    redisTemplate.delete(key);
                                } else {
                                    redisTemplate.expireAt(key, endTime);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //执行事务
                            List<Object> list = operations.exec();
                            if (list != null) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    };
                    return redisTemplate.execute(sessionCallback);
                } else {
                    return false;
                }
            } else {
                Ratelimit ratelimit = findRateLimitById(id);
                //如果没有key说明上一次到期了redis自动消除了 在重新新增一个
                RateLimitCount rateLimitCount = new RateLimitCount();
                rateLimitCount.setStartTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                String endTime = ratelimit.getEndTime();
                rateLimitCount.setEndTime(endTime);
                rateLimitCount.setCount(ratelimit.getAccessCount() - 1);
                redisTemplate.opsForValue().set(key, JSON.toJSONString(rateLimitCount));
                redisTemplate.expireAt(key, simpleDateFormat.parse(endTime));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Integer getCount(String id, String requestIp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String key = redisPreCount + id + and + requestIp;
        String value = redisTemplate.opsForValue().get(key);
        RateLimitCount rateLimitCount = JSON.parseObject(value, RateLimitCount.class);
        try {
            Date endTime = simpleDateFormat.parse(rateLimitCount.getEndTime());
            if (new Date().after(endTime)) {
                redisTemplate.delete(key);
            } else {
                redisTemplate.expireAt(key, endTime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rateLimitCount.getCount();
    }

    /**
     * @param id
     * @return true 返回时已经达到上限 false表示没有
     */
    @Override
    public Boolean isUpperLimit(String id, String requestIp) {
        //不存在key的时候默认是可以的
        String key = redisPreCount + id + and + requestIp;
        if (!redisTemplate.hasKey(key)) {
            return false;
        }
        return getCount(id,requestIp) == 0 ? true : false;
    }

    public StringRedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String id(String id) {
        return new StringBuffer(redisPre + id).toString();
    }


}
