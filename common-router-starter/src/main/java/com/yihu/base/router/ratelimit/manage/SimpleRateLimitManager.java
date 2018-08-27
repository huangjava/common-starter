package com.yihu.base.router.ratelimit.manage;

import com.yihu.base.router.model.Result;
import com.yihu.base.router.ratelimit.IPRateLimiter;
import com.yihu.base.router.ratelimit.IRatelimiter;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * Created by chenweida on 2018/4/27 0027.
 */
public class SimpleRateLimitManager extends AbstractRateLimitManager {
    //全部的规则校验器
    private Set<IRatelimiter> ratelimiters = null;

    //根据规则校验看当前的路径是否符合
    @Override
    public Result dofilter(HttpServletRequest request) {
        for (IRatelimiter ratelimiter : ratelimiters) {
            Result result=ratelimiter.rateLimit(request);
            if (!result.getSuccess()) {
                return result;
            }
        }
        return new Result();
    }

    public Set<IRatelimiter> getRatelimiters() {
        return ratelimiters;
    }

    public void setRatelimiters(Set<IRatelimiter> ratelimiters) {
        this.ratelimiters = ratelimiters;
    }
}
