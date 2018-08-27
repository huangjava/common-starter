package com.yihu.admin.server.zuul;

import com.yihu.admin.server.eureka.DiscoveryService;
import com.yihu.admin.server.model.RuleRoute;
import com.yihu.admin.server.model.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by chenweida on 2018/5/15 0015.1
 */
public class RouterCreater {
    private Logger logger= LoggerFactory.getLogger(RouterCreater.class);
    private DiscoveryService discoveryService;

    public RouterCreater(DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }


    public List<RuleRoute> create() {
        List<RuleRoute> ruleRoutes = new ArrayList<>();

        List<Server> servers = discoveryService.listServers();

        for (Server server : servers) {
            RuleRoute ruleRoute = new RuleRoute();
            ruleRoute.setId(server.getUuid());
            ruleRoute.setPath("/" + server.getUuid() + "/**");
            ruleRoute.setUrl("http://"+server.getIp()+":"+server.getPort()+"/");
            ruleRoute.setEnabled(true);
            ruleRoute.setRetryable(true);
            logger.debug(ruleRoute.toString());
            ruleRoutes.add(ruleRoute);
        }

        return ruleRoutes;
    }
}
