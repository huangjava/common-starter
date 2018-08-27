package com.yihu.admin.server.eureka;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.yihu.admin.server.model.Server;
import com.yihu.admin.server.model.MicroService;
import com.yihu.admin.server.properties.AdminServerProperties;
import com.yihu.base.es.config.ElastricSearchHelper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chenweida on 2018/5/5 0005.1
 */
@RestController
@RequestMapping(value = "/discovery", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class DiscoveryController {
    @Autowired
    private DiscoveryService discoveryService;
    @Autowired
    private ElastricSearchHelper elastricSearchHelper;
    @Autowired
    private AdminServerProperties adminServerProperties;

    @GetMapping("getServer")
    public String getServer(
            @RequestParam(value = "uuid", required = true) String uuid
    ) {
        return JSONObject.fromObject(discoveryService.getServer(uuid)).toString();
    }

    /**
     * 获取发现服务的信息
     *
     * @return
     */
    @GetMapping("listMicroService")
    public String listMicroService() {
        JSONObject jo=new JSONObject();

        jo.put("microservers",JSONArray.fromObject(discoveryService.listServers()).toString());
        jo.put("servers",JSONArray.fromObject(discoveryService.listMicroServices()).toString());
        return jo.toString();
    }

    @PostMapping("addMicroService")
    public Boolean addMicroService(
            @RequestParam(value = "ip", required = true) String ip,
            @RequestParam(value = "port", required = true) Integer port,
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "healthCheckUrl", required = false) String healthCheckUrl

    ) {
        return discoveryService.add(ip, port, name, healthCheckUrl);
    }

    @GetMapping("index_v1_num")
    public String index_v1_num() {
        JSONObject jo = new JSONObject();
        Integer microNum = 0;
        Integer serverNum = 0;
        Integer downNum = 0;
        Integer upNum = 0;
        Integer startingNum = 0;
        Integer outNum = 0;
        Integer unknowNum = 0;

        List<MicroService> serviceModels = discoveryService.listMicroServices();
        List<String> downList = new ArrayList<>();
        List<String> startingList = new ArrayList<>();
        List<String> outList = new ArrayList<>();
        List<String> unknowList = new ArrayList<>();
        microNum = serviceModels.size();
        List<Server> servers = discoveryService.listServers();
        serverNum = servers.size();
        for (Server server : servers) {
            if ("up".equals(server.getStatus().toLowerCase())) {
                upNum++;
            }
            if ("down".equals(server.getStatus().toLowerCase())) {
                downNum++;
                downList.add(server.getAppName()+"("+server.getInstanceId()+")");
            }
            if ("starting".equals(server.getStatus().toLowerCase())) {
                startingNum++;
                startingList.add(server.getAppName()+"("+server.getInstanceId()+")");
            }
            if ("out_of_service".equals(server.getStatus().toLowerCase())) {
                outNum++;
                outList.add(server.getAppName()+"("+server.getInstanceId()+")");
            }
            if ("unknown".equals(server.getStatus().toLowerCase())) {
                unknowNum++;
                unknowList.add(server.getAppName()+"("+server.getInstanceId()+")");
            }

        }
        jo.put("microNum", microNum);
        jo.put("serverNum", serverNum);
        jo.put("downNum", downNum);
        jo.put("upNum", upNum);
        jo.put("downList", downList);
        jo.put("downList", downList);
        jo.put("startingNum", startingNum);
        jo.put("startingList", startingList);
        jo.put("outNum", outNum);
        jo.put("outList", outList);
        jo.put("unknowNum", unknowNum);
        jo.put("unknowList", unknowList);
        return jo.toString();
    }
}
