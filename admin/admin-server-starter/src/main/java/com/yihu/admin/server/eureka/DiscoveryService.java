package com.yihu.admin.server.eureka;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.yihu.admin.server.manage.ServerManager;
import com.yihu.admin.server.model.MicroService;
import com.yihu.admin.server.model.Server;
import com.yihu.admin.server.util.MD5;
import net.sf.json.JSONArray;
import org.apache.tomcat.util.security.MD5Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by chenweida on 2018/5/15 0015.1
 */
@Service
public class DiscoveryService {
    private Logger logger = LoggerFactory.getLogger(DiscoveryService.class);

    @Autowired
    private ServerManager serverManager;

    public Server getServer(String uuid) {
        return serverManager.getServer(uuid);
    }

    /**
     * 获取注册中心全部的微服务
     *
     * @return
     */
    public List<MicroService> listMicroServices() {
        return serverManager.listMicroService();
    }

    public Boolean add(String ip, Integer port, String name, String healthCheckUrl) {
        try {
            serverManager.addMicroService(ip, port, name, healthCheckUrl);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * 获取注册中心全部的服务器
     *
     * @return
     */
    public List<Server> listServers() {
        return serverManager.listServers();
    }

}
