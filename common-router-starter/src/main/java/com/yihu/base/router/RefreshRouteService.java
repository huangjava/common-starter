package com.yihu.base.router;

import com.yihu.base.router.route.SimplerRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.event.InstanceRegisteredEvent;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by chenweida on 2018/4/19 0019.
 */
public class RefreshRouteService {
    @Resource(name = "zuulDiscoveryRefreshRoutesListener")
    ApplicationListener publisher;

    @Autowired
    private   SimplerRouter routeLocator;
    @Autowired
    private ZuulHandlerMapping zuulHandlerMapping;

    public void refreshRoute() {
        InstanceRegisteredEvent instanceRegisteredEvent=new InstanceRegisteredEvent(routeLocator,null);
        publisher.onApplicationEvent(instanceRegisteredEvent);
    }
}
