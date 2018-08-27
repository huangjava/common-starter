package com.yihu.admin.server.config;

import com.yihu.admin.server.eureka.DiscoveryService;
import com.yihu.admin.server.zuul.RouterCreater;
import com.yihu.admin.server.zuul.SimplerRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by chenweida on 2018/5/15 0015.1
 */
@EnableZuulProxy
@Configuration
public class ZuulAutoConfig {
    @Autowired
    ZuulProperties zuulProperties;
    @Autowired
    ServerProperties server;
    @Autowired
    private DiscoveryService discoveryService;

    @Bean
    public RouterCreater routerCreater() {
        return new RouterCreater(discoveryService);
    }

    @Bean
    public SimplerRouter simplerRouter() {
        SimplerRouter simplerRouter = new SimplerRouter(this.server.getServletPrefix(), this.zuulProperties);
        simplerRouter.setRouterCreater(routerCreater());
        return simplerRouter;
    }

}
