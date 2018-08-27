package com.yihu.base.router.rule.storage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.JSONToken;
import com.yihu.base.router.model.RuleRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by chenweida on 2018/4/27 0027.
 * redis规则存储器
 */
public class RedisRuleStorage extends AbstractRuleStorage {
    private Logger logger = LoggerFactory.getLogger(RedisRuleStorage.class);
    private StringRedisTemplate redisTemplate;
    private static String redisPre = "router:rule:";

    @Override
    public Boolean saveRuleStorage(RuleRoute ruleRoute) {
        try {
            if (StringUtils.isEmpty(ruleRoute.getId())) {
                logger.error("RuleRoute id is null");
                return false;
            }
            String key = id(ruleRoute.getId());
            Object value = redisTemplate.opsForValue().get(key.toString());
            if (!StringUtils.isEmpty(value)) {
                logger.error("RuleRoute id is exists ");
                return false;
            }

            redisTemplate.opsForValue().set(key.toString(), JSON.toJSON(ruleRoute).toString());
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean deleteRuleStorageById(String id) {
        try {
            redisTemplate.delete(id(id));
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean updateRuleStorage(RuleRoute ruleRoute) {
        try {
            if (StringUtils.isEmpty(ruleRoute.getId())) {
                logger.error("RuleRoute id is null");
                return false;
            }
            String key = id(ruleRoute.getId());

            redisTemplate.opsForValue().set(key.toString(), JSON.toJSON(ruleRoute).toString());
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public RuleRoute getRuleStorageById(String id) {
        String json = redisTemplate.opsForValue().get(id(id));
        return JSON.parseObject(json, RuleRoute.class);
    }

    @Override
    public List<RuleRoute> listRuleStorage() {
        Set<String> keys = redisTemplate.keys(redisPre + "*");
        List<String> values = redisTemplate.opsForValue().multiGet(keys);
        List<RuleRoute> ruleRoutes = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            ruleRoutes.add(JSON.parseObject(values.get(i), RuleRoute.class));
        }

        return ruleRoutes;
    }

    @Override
    public Integer countRuleStorage() {
        Set<String> keys = redisTemplate.keys(redisPre + "*");
        return keys.size();
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
