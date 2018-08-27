package com.yihu.base.es.properties;

import com.yihu.base.common.RollingUtil;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Created by chenweida on 2018/2/24.
 * ES相关配置
 */
public class ElasticsearchProperties {
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    private String hosts; //http://59.61.92.90:9065,http://59.61.92.90:9067
    private String index;
    private String type;
    private String clusterName;
    private Integer maxTotalConnection = 50;//最大连接数
    private Integer maxConnectionIdleTime = 10;//链接最大等待时间
    private Integer connTimeout = 60 * 1000 * 10;//链接超时时间 默认10分种
    private Integer readTimeout = 60 * 1000 * 10;//60秒
    private Boolean multiThreaded = true;//是否多线程
    private Boolean discoveryEnabled = false;//是否开启自动发现模式  集群模式使用
    private String rolling;//日志按照什么滚动  day week month year

    public String getRolling() {
        return rolling;
    }

    public void setRolling(String rolling) {
        this.rolling = rolling;
    }

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public String getIndex() {
        return RollingUtil.getRollingAppendLast(index,rolling);
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public Integer getMaxTotalConnection() {
        return maxTotalConnection;
    }

    public void setMaxTotalConnection(Integer maxTotalConnection) {
        this.maxTotalConnection = maxTotalConnection;
    }

    public Integer getMaxConnectionIdleTime() {
        return maxConnectionIdleTime;
    }

    public void setMaxConnectionIdleTime(Integer maxConnectionIdleTime) {
        this.maxConnectionIdleTime = maxConnectionIdleTime;
    }

    public Integer getConnTimeout() {
        return connTimeout;
    }

    public void setConnTimeout(Integer connTimeout) {
        this.connTimeout = connTimeout;
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Boolean getMultiThreaded() {
        return multiThreaded;
    }

    public void setMultiThreaded(Boolean multiThreaded) {
        this.multiThreaded = multiThreaded;
    }

    public Boolean getDiscoveryEnabled() {
        return discoveryEnabled;
    }

    public void setDiscoveryEnabled(Boolean discoveryEnabled) {
        this.discoveryEnabled = discoveryEnabled;
    }

    public static void main(String[] args) {
        System.out.println( LocalDate.now().minusWeeks(0).with(DayOfWeek.MONDAY));
    }
}
