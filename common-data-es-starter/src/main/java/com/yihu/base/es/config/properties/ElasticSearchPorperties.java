package com.yihu.base.es.config.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by chenweida on 2018/5/3 0003.
 */
@Component
public class ElasticSearchPorperties {
    @Value("${spring.data.elasticsearch.cluster-name:jkzl}")
    private String clusterName;//http://172.19.103.68:9200  #多个逗号分割

    @Value("${spring.data.elasticsearch.cluster-nodes:http://172.19.103.68:9300}")
    private String clusterNodes;//http://172.19.103.68:9200  #多个逗号分割

    @Value("${spring.data.elasticsearch.cluster-nodes-jest:http://172.19.103.68:9200}")
    private String clusterNodesJest;//http://172.19.103.68:9200  #多个逗号分割

    @Value("${spring.data.elasticsearch.jest.discovery-enabled:true}")
    private Boolean jestDiscoveryEnabled; //#开启嗅探

    @Value("${spring.data.elasticsearch.jest.conn-timeout:60}")
    private Integer jestConnTimeout;//连接时间单位是秒

    @Value("${spring.data.elasticsearch.jest.read-timeout:60}")
    private Integer jestReadTimeout;//读取时间单位是秒

    @Value("${spring.data.elasticsearch.jest.multith-readed:true}")
    private Boolean jestMultiThreaded;//开启多线程

    @Value("${spring.data.elasticsearch.jest.max-total-connection:30}")
    private Integer jestMaxTotalConnection;//最大连接

    @Value("${spring.data.elasticsearch.jest.max-connection-idle-time:60}")
    private Integer jestMaxConnectionIdleTime;//最大等待时间

    public String getClusterNodesJest() {
        return clusterNodesJest;
    }

    public void setClusterNodesJest(String clusterNodesJest) {
        this.clusterNodesJest = clusterNodesJest;
    }

    public Boolean getJestDiscoveryEnabled() {
        return jestDiscoveryEnabled;
    }

    public void setJestDiscoveryEnabled(Boolean jestDiscoveryEnabled) {
        this.jestDiscoveryEnabled = jestDiscoveryEnabled;
    }

    public Integer getJestConnTimeout() {
        return jestConnTimeout;
    }

    public void setJestConnTimeout(Integer jestConnTimeout) {
        this.jestConnTimeout = jestConnTimeout;
    }

    public Integer getJestReadTimeout() {
        return jestReadTimeout;
    }

    public void setJestReadTimeout(Integer jestReadTimeout) {
        this.jestReadTimeout = jestReadTimeout;
    }

    public Boolean getJestMultiThreaded() {
        return jestMultiThreaded;
    }

    public void setJestMultiThreaded(Boolean jestMultiThreaded) {
        this.jestMultiThreaded = jestMultiThreaded;
    }

    public Integer getJestMaxTotalConnection() {
        return jestMaxTotalConnection;
    }

    public void setJestMaxTotalConnection(Integer jestMaxTotalConnection) {
        this.jestMaxTotalConnection = jestMaxTotalConnection;
    }

    public Integer getJestMaxConnectionIdleTime() {
        return jestMaxConnectionIdleTime;
    }

    public void setJestMaxConnectionIdleTime(Integer jestMaxConnectionIdleTime) {
        this.jestMaxConnectionIdleTime = jestMaxConnectionIdleTime;
    }

    public String getClusterNodes() {
        return clusterNodes;
    }

    public void setClusterNodes(String clusterNodes) {
        this.clusterNodes = clusterNodes;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }
}
