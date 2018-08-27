package com.yihu.base.router.ratelimit.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.yihu.base.router.model.Result;
import com.yihu.base.router.ratelimit.manage.SimpleRateLimitManager;
import com.yihu.base.router.util.NetworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by chenweida on 2018/4/27 0027.
 * 规则过滤器
 */
public class RateLimitFilter extends ZuulFilter {
    private Logger logger = LoggerFactory.getLogger(RateLimitFilter.class);

    private SimpleRateLimitManager rateLimitManager;

    @Override
    public String filterType() {
        //filterType：返回过滤器的类型。有pre、route、post、error等几种取值

        return "pre";
    }

    @Override
    public int filterOrder() {
        //回一个int值来指定过滤器的执行顺序，不同的过滤器允许返回相同的数字
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        //返回一个boolean值来判断该过滤器是否要执行，true表示执行，false表示不执行。
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        //这边可以根据数据库的配置去判断当前的路径是否是限制的
        Result result = rateLimitManager.dofilter(request);
        if (result.getSuccess()) {
            ctx.setSendZuulResponse(true);
            ctx.setResponseStatusCode(200);
        } else {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(200);
            ctx.getResponse().setCharacterEncoding("UTF-8");
            ctx.setResponseBody("{\"message\":\"" + result.getMessage() + "\",\"status\":"+result.getStatus()+"}");
        }
        return null;
    }

    public SimpleRateLimitManager getRateLimitManager() {
        return rateLimitManager;
    }

    public void setRateLimitManager(SimpleRateLimitManager rateLimitManager) {
        this.rateLimitManager = rateLimitManager;
    }
}
