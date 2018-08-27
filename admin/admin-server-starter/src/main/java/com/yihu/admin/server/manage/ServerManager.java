package com.yihu.admin.server.manage;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.yihu.admin.server.model.MicroService;
import com.yihu.admin.server.model.Server;
import com.yihu.admin.server.util.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenweida on 2018/5/17 0017.1
 */
@Component
public class ServerManager {

    private Logger logger = LoggerFactory.getLogger(ServerManager.class);
    @Autowired
    private EurekaClient eurekaClient;

    private volatile Map<String, MicroService> microServices = new HashMap<>();


    public Boolean addMicroService(
            String ip,
            Integer port,
            String name,
            String healthCheckUrl) {
        try {
            if (StringUtils.isEmpty(healthCheckUrl)) {
                healthCheckUrl = "http://" + ip + ":" + port + "/health";
            }
            String instantsid = ip + ":" + port;
            MicroService microService = new MicroService(name);
            Server server = new Server();
            server.setIp(ip);
            server.setPort(port);
            server.setHealthCheckUrl(healthCheckUrl);
            server.setUuid(MD5.GetMD5Code(microService.getName() + "-" + instantsid));
            server.setInstanceId(instantsid);
            server.setStatus("unknow");
            microService.getServers().add(server);

            if (microServices.containsKey(microService.getName())) {
                microService = microServices.get(microService.getName());
                microService.getServers().add(server);
            }
            microServices.put(microService.getName(), microService);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<MicroService> listMicroService() {
        return new ArrayList<>(microServices.values());
    }

    /**
     * 定时获取eureka中的服务
     *
     * @return
     */
    @PostConstruct
    public Boolean init() {
        new Thread(new ServerHeart(eurekaClient, microServices)).start();
        logger.info("ServerHeart start !!!!!!!!!!!");
        return true;
    }

    public List<Server> listServers() {
        List<Server> serviceModels = new ArrayList<>();
        for (MicroService microService : microServices.values()) {
            for (Server server : microService.getServers()) {
                serviceModels.add(server);
            }
        }
        return serviceModels;
    }

    public Server getServer(String uuid) {
        for (MicroService microService : microServices.values()) {
            for (Server server : microService.getServers()) {
               if(uuid.equals(server.getUuid())){
                   return server;
               }
            }
        }
        return null;
    }
}
