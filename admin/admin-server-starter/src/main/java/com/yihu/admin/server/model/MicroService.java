package com.yihu.admin.server.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenweida on 2018/5/15 0015.1
 * 微服务实体
 */
public class MicroService {

    private String name;//微服务名称
    private List<Server> servers = new ArrayList<>();//服务器实体

    public MicroService(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Server> getServers() {
        return servers;
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }
}
