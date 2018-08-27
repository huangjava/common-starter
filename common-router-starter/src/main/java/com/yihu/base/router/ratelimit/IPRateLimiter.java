package com.yihu.base.router.ratelimit;

import com.yihu.base.router.model.Ratelimit;
import com.yihu.base.router.model.Result;
import com.yihu.base.router.model.RuleRoute;
import com.yihu.base.router.ratelimit.storage.IRateLimitStorage;
import com.yihu.base.router.util.IpV4Util;
import com.yihu.base.router.util.NetworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by chenweida on 2018/4/27 0027.
 * IP 规则验证器
 */
public class IPRateLimiter implements IRatelimiter {
    private Logger logger = LoggerFactory.getLogger(IPRateLimiter.class);

    private IRateLimitStorage rateLimitStorage;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Result rateLimit(HttpServletRequest request) {
        try {
            //获取请求的的链接
            String path = request.getRequestURI();
            //获取ip
            String requestIP = NetworkUtil.getIpAddress(request);
            logger.info("ip:" + requestIP + ",path:" + path);
            //获取数据库规则
            List<Ratelimit> ratelimitList = rateLimitStorage.findAllRatelimit();
            //便利判断
            if (ratelimitList != null && ratelimitList.size() > 0) {
                for (int i = 0; i < ratelimitList.size(); i++) {
                    Ratelimit ratelimit = ratelimitList.get(i);
                    //1.验证当前请求是否符合规则 当前请求不是需要认证的请求那就说明是可以访问的
                    if (!antPathMatcher.match(ratelimit.getPath(), path)) {
                        continue;
                    }
                    //2.判断IP是否可以通过
                    //如果IP即在 通过列表也在 不通过列表 那也默认不可以通过
                    if (requestIpIsInclude(requestIP, ratelimit.getIncludeIp()) && requestIpIsExclude(requestIP, ratelimit.getExcludeIp())) {
                        //3 判断是否有次数限制
                        //默认是0 表示没有限制
                        if (ratelimit.getAccessCount() == 0) {
                            return new Result();
                        }
                        //如果没有超过次数限制
                        if (!rateLimitStorage.isUpperLimit(ratelimit.getId(),requestIP)) {
                            if (!rateLimitStorage.countDec(ratelimit.getId(),requestIP)) {
                                return new Result(501, "Request upper limit");
                            }
                            return new Result();
                        } else {
                            return new Result(501, "Request upper limit");
                        }

                    } else {
                        return new Result(502, "IP:" + requestIP + " not authority");
                    }
                }
            }

            return new Result();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new Result(500, e.getMessage());
        }
    }

    /**
     * @param requestIP
     * @param includeIp
     * @return
     */
    private boolean requestIpIsInclude(String requestIP, String includeIp) {
        //1 如果includeIp是空默认是通过
        if (StringUtils.isEmpty(includeIp)) {
            return true;
        }
        String[] includeIps=includeIp.split(",");
        //2 如果IP属于这个includeIp 表示通过
        for(String oneIncludeIp:includeIps){
            if(IpV4Util.ipExistsInRange(requestIP,oneIncludeIp)){
                return true;
            }
        }
        return false;
    }

    /**
     * @param requestIP
     * @param ExcludIp
     * @return
     */
    private boolean requestIpIsExclude(String requestIP, String ExcludIp) {
        //1 如果ExcludIp是空默认是通过
        if (StringUtils.isEmpty(ExcludIp)) {
            return true;
        }
        String[] excludeIps=ExcludIp.split(",");
        //2如果IP属于这个ExcludIp 表示不通过
        for(String oneIncludeIp:excludeIps){
            if(IpV4Util.ipExistsInRange(requestIP,oneIncludeIp)){
                return false;
            }
        }
        return true;
    }

    public IRateLimitStorage getRateLimitStorage() {
        return rateLimitStorage;
    }

    public void setRateLimitStorage(IRateLimitStorage rateLimitStorage) {
        this.rateLimitStorage = rateLimitStorage;
    }
}
