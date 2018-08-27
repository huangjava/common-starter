package com.yihu.base.router.route;

import com.yihu.base.router.model.RuleRoute;
import com.yihu.base.router.rule.manager.SimpleRuleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenweida on 2018/4/27 0027.
 * 简单的实现路由器
 */
public class SimplerRouter extends SimpleRouteLocator implements RefreshableRouteLocator {
    public final static Logger logger = LoggerFactory.getLogger(SimplerRouter.class);

    private ZuulProperties properties;

    private SimpleRuleManager simpleRuleManager;

    public SimplerRouter(String servletPath, ZuulProperties properties) {
        super(servletPath, properties);
        this.properties = properties;
        logger.info("servletPath:{}", servletPath);
    }

    @Override
    public void refresh() {
        super.doRefresh();
    }

    @Override
    protected Map<String, ZuulProperties.ZuulRoute> locateRoutes() {
        LinkedHashMap<String, ZuulProperties.ZuulRoute> routesMap = new LinkedHashMap<String, ZuulProperties.ZuulRoute>();
        //从application.properties中加载路由信息  禁用掉
        //routesMap.putAll(super.locateRoutes());
        //从db中加载路由信息
        routesMap.putAll(locateRoutesFromDB());
        //优化一下配置
        LinkedHashMap<String, ZuulProperties.ZuulRoute> values = new LinkedHashMap<String, ZuulProperties.ZuulRoute>();

        for (Map.Entry<String, ZuulProperties.ZuulRoute> entry : routesMap.entrySet()) {
            String path = entry.getKey();
            // Prepend with slash if not already present.
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (StringUtils.hasText(this.properties.getPrefix())) {
                path = this.properties.getPrefix() + path;
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
            }
            values.put(path, entry.getValue());
        }
        return values;
    }

    /**
     * 模拟从数据库加载数据
     *
     * @return
     */
    private Map<String, ZuulProperties.ZuulRoute> locateRoutesFromDB() {
        Map<String, ZuulProperties.ZuulRoute> routes = new LinkedHashMap<String, ZuulProperties.ZuulRoute>();
        //现在是写死在代码中，这边可以改成用数据库获取数据
        List<RuleRoute> results = simpleRuleManager.getAllRules();

        for (RuleRoute result : results) {
            //判断配置是否正确
            if (StringUtils.isEmpty(result.getPath())) {
                logger.error(" id: " + result.getId() + " path 为空");
                continue;
            }
            if (StringUtils.isEmpty(result.getServiceId()) && StringUtils.isEmpty(result.getUrl())) {
                logger.error(" id: " + result.getId() + " serviceID,url 都为空");
                continue;
            }
            ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();
            try {
                BeanUtils.copyProperties(result, zuulRoute);
            } catch (Exception e) {
                logger.error("=============从数据库加载Zuul配置失败==============", e);
            }
            logger.info(result.toString());
            routes.put(zuulRoute.getPath(), zuulRoute);
        }
        return routes;
    }

    public SimpleRuleManager getSimpleRuleManager() {
        return simpleRuleManager;
    }

    public void setSimpleRuleManager(SimpleRuleManager simpleRuleManager) {
        this.simpleRuleManager = simpleRuleManager;
    }
}
