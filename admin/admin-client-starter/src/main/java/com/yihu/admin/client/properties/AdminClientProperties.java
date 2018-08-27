package com.yihu.admin.client.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by chenweida on 2018/5/15 0015.1
 */
@Component
public class AdminClientProperties {
    @Value("${yihu.admin.client.server.ip:127.0.0.1}")
    private String ip;//ip
    @Value("${yihu.admin.client.server.port:8082}")
    private Integer port;//port
    @Value("${yihu.admin.client.advice.sql.springJDBC:false}")
    private Boolean sql;//port
    @Value("${yihu.admin.client.advice.sql.hibernate:false}")
    private Boolean hibernateSql;//port
    @Value("${yihu.admin.client.advice.zuul.pre:false}")
    private Boolean zuulPre;//port
    @Value("${yihu.admin.client.advice.zuul.post:false}")
    private Boolean zuulPost;//port
    @Value("${yihu.admin.client.advice.zuul.route:false}")
    private Boolean zuulRoute;//port
    @Value("${yihu.admin.client.advice.http:true}")
    private Boolean http;//port
    @Value("${yihu.admin.client.buffer.size:100000}")
    private Integer bufferSize;//port

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Boolean getSql() {
        return sql;
    }

    public void setSql(Boolean sql) {
        this.sql = sql;
    }

    public Boolean getHttp() {
        return http;
    }

    public void setHttp(Boolean http) {
        this.http = http;
    }

    public Boolean getZuulPre() {
        return zuulPre;
    }

    public void setZuulPre(Boolean zuulPre) {
        this.zuulPre = zuulPre;
    }

    public Boolean getZuulPost() {
        return zuulPost;
    }

    public void setZuulPost(Boolean zuulPost) {
        this.zuulPost = zuulPost;
    }

    public Boolean getHibernateSql() {
        return hibernateSql;
    }

    public void setHibernateSql(Boolean hibernateSql) {
        this.hibernateSql = hibernateSql;
    }

    public Boolean getZuulRoute() {
        return zuulRoute;
    }

    public void setZuulRoute(Boolean zuulRoute) {
        this.zuulRoute = zuulRoute;
    }

    public Integer getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(Integer bufferSize) {
        this.bufferSize = bufferSize;
    }
}
