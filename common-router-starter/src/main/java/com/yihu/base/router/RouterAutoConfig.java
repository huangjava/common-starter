package com.yihu.base.router;

import com.yihu.base.router.ratelimit.IPRateLimiter;
import com.yihu.base.router.ratelimit.IRatelimiter;
import com.yihu.base.router.ratelimit.filter.RateLimitFilter;
import com.yihu.base.router.ratelimit.manage.SimpleRateLimitManager;
import com.yihu.base.router.ratelimit.storage.AbstractRateLimitStorage;
import com.yihu.base.router.ratelimit.storage.IRateLimitStorage;
import com.yihu.base.router.ratelimit.storage.RedisRateLimitStorage;
import com.yihu.base.router.route.SimplerRouter;
import com.yihu.base.router.rule.manager.SimpleRuleManager;
import com.yihu.base.router.rule.storage.AbstractRuleStorage;
import com.yihu.base.router.rule.storage.RedisRuleStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;

/**
 * Created by chenweida on 2018/4/27 0027.
 */
@EnableZuulProxy
@Import({
        RouteEndPoint.class,
        RefreshRouteService.class})
public class RouterAutoConfig {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    ZuulProperties zuulProperties;
    @Autowired
    ServerProperties server;


    //--------------------------限制规则相关配置 start---------------------------
    @Bean
    @ConditionalOnMissingBean
    public IRateLimitStorage redisRateLimitStorage() {
        RedisRateLimitStorage redisRateLimitStorage = new RedisRateLimitStorage();
        redisRateLimitStorage.setRedisTemplate(redisTemplate);
        return redisRateLimitStorage;
    }
    @Bean
    @ConditionalOnMissingBean
    public SimpleRateLimitManager simpleRateLimitManager(Set<IRatelimiter> ratelimiters) {
        SimpleRateLimitManager simpleRateLimitManager = new SimpleRateLimitManager();
        simpleRateLimitManager.setRatelimiters(ratelimiters);
        return simpleRateLimitManager;
    }

    @Bean
    public IPRateLimiter ipRateLimiter(){
        IPRateLimiter rateLimiter= new IPRateLimiter();
        rateLimiter.setRateLimitStorage(redisRateLimitStorage());
        return rateLimiter;
    }
    //--------------------------限制规则相关配置 end---------------------------


    //--------------------------路由规则相关配置 start---------------------------


    @Bean
    @ConditionalOnMissingBean
    public AbstractRuleStorage ruleStorage() {
        RedisRuleStorage redisRuleStorage = new RedisRuleStorage();
        redisRuleStorage.setRedisTemplate(redisTemplate);
        return redisRuleStorage;
    }

    @Bean
    @ConditionalOnMissingBean
    public SimpleRuleManager simpleRuleManager() {
        SimpleRuleManager simpleRuleManager = new SimpleRuleManager();
        simpleRuleManager.setRuleStorage(ruleStorage());
        return simpleRuleManager;
    }

    @Bean
    @ConditionalOnMissingBean
    public SimplerRouter simplerRouter() {
        SimplerRouter simplerRouter = new SimplerRouter(this.server.getServletPrefix(), this.zuulProperties);
        simplerRouter.setSimpleRuleManager(simpleRuleManager());
        return simplerRouter;
    }
    //--------------------------路由规则相关配置 end---------------------------
    @Bean
    public RateLimitFilter rateLimitFilter(Set<IRatelimiter> ratelimiters) {
        RateLimitFilter rateLimitFilter = new RateLimitFilter();
        rateLimitFilter.setRateLimitManager(simpleRateLimitManager(ratelimiters));
        return rateLimitFilter;
    }

}
