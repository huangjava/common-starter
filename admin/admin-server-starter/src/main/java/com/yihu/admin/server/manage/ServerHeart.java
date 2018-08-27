package com.yihu.admin.server.manage;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.yihu.admin.server.model.MicroService;
import com.yihu.admin.server.model.Server;
import com.yihu.admin.server.util.DateUtil;
import com.yihu.admin.server.util.HttpClientUtil;
import com.yihu.admin.server.util.MD5;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by chenweida on 2018/5/17 0017.1
 */
public class ServerHeart implements Runnable {
    private Logger logger = LoggerFactory.getLogger(ServerHeart.class);

    private EurekaClient eurekaClient;
    private Map<String, MicroService> microServices;
    private static final String eureka_pre = "eureka_";
    private static final Long sleepTime = 10 * 1000L;
    private HttpClientUtil restTemplate = new HttpClientUtil();

    public ServerHeart(EurekaClient eurekaClient, Map<String, MicroService> microServices) {
        this.eurekaClient = eurekaClient;
        this.microServices = microServices;
    }

    @Override
    public void run() {
        while (true) {
            try {
                //先去eureka获取数据
                for (Application application : eurekaClient.getApplications().getRegisteredApplications()) {
                    String key = eureka_pre + application.getName();
                    MicroService microService = null;
                    if (microServices.containsKey(key)) {
                        microService = microServices.get(key);
                        List<InstanceInfo> instanceInfos = application.getInstancesAsIsFromEureka();
                        List<Server> servers=new ArrayList<>();
                        for (InstanceInfo instanceInfo : instanceInfos) {
                            Server server = new Server();
                            BeanUtils.copyProperties(instanceInfo, server);
                            server.setStatus(instanceInfo.getStatus().toString());
                            server.setIp(instanceInfo.getIPAddr());
                            server.setUuid(MD5.GetMD5Code(microService.getName() + "-" + instanceInfo.getInstanceId()));
                            logger.debug(server.toString());
                            servers.add(server);
                        }
                        microService.setServers(servers);
                    } else {
                        microService = new MicroService(application.getName());
                        List<InstanceInfo> instanceInfos = application.getInstancesAsIsFromEureka();
                        for (InstanceInfo instanceInfo : instanceInfos) {
                            Server server = new Server();
                            BeanUtils.copyProperties(instanceInfo, server);
                            server.setStatus(instanceInfo.getStatus().toString());
                            server.setIp(instanceInfo.getIPAddr());
                            server.setUuid(MD5.GetMD5Code(microService.getName() + "-" + instanceInfo.getInstanceId()));
                            logger.debug(server.toString());
                            microService.getServers().add(server);
                        }
                    }

                    microServices.put(key, microService);
                }
                //如果不是从eureka获取的自己在手动去更新状态
                for (Map.Entry<String, MicroService> one : microServices.entrySet()) {
                    for (Server server : one.getValue().getServers()) {
                        try {
                            JSONObject jsonObject = JSONObject.fromObject(httpGet(server));
                            String status = jsonObject.getString("status");
                            server.setStatus(status);
                            Long time=new Date().getTime();
                            server.setLastUpdatedTimestamp(time);
                            server.setLastUpdatedTimestampStr((DateUtil.dateToStr(new Date(time), DateUtil.YYYY_MM_DD_HH_MM_SS_SSS)));
                        } catch (Exception e) {
                            server.setStatus("OUT_OF_SERVICE");
                            Long time=new Date().getTime();
                            server.setLastUpdatedTimestamp(time);
                            server.setLastUpdatedTimestampStr((DateUtil.dateToStr(new Date(time), DateUtil.YYYY_MM_DD_HH_MM_SS_SSS)));
                        }
                    }
                }
                logger.debug("MicroService size :" + microServices.size());
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String httpGet(Server server) {
        try {
            //设置header
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            String result = restTemplate.get(server.getHealthCheckUrl(), "UTF-8");
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "";
        }
    }
}
