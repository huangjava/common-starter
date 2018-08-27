package com.yihu.base.router.ratelimit.manage;

import com.yihu.base.router.model.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenweida on 2018/4/27 0027.
 */
public class AbstractRateLimitManager implements IRateLimitManager {
    @Override
    public Result dofilter(HttpServletRequest request) {
        return null;
    }
}
