package com.yihu.base.router.ratelimit.manage;

import com.yihu.base.router.model.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenweida on 2018/4/27 0027.
 */
public interface IRateLimitManager {

    Result dofilter(HttpServletRequest request);
}
