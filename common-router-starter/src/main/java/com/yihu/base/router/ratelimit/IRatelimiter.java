package com.yihu.base.router.ratelimit;

import com.yihu.base.router.model.Result;
import com.yihu.base.router.model.RuleRoute;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenweida on 2018/4/27 0027.
 * 限制器抽象类
 */
public interface IRatelimiter {
    /**
     * 限制规则
     * @return
     */
    Result rateLimit(HttpServletRequest httpServletRequest);
}
