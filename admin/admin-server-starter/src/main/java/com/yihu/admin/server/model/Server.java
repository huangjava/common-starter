package com.yihu.admin.server.model;

import com.netflix.appinfo.DataCenterInfo;
import com.netflix.appinfo.LeaseInfo;
import com.netflix.discovery.converters.Auto;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chenweida on 2018/5/15 0015.1
 */
public class Server {

    private String uuid;
    private String appName;
    private String appGroupName;
    private String sid;
    private int securePortT;
    private String homePageUrl;
    private String statusPageUrl;
    private String healthCheckUrl;
    private String secureHealthCheckUrl;
    private String vipAddress;
    private String secureVipAddress;
    private String statusPageRelativeUrl;
    private String statusPageExplicitUrl;
    private String healthCheckRelativeUrl;
    private String healthCheckSecureExplicitUrl;
    private String vipAddressUnresolved;
    private String secureVipAddressUnresolved;
    private String healthCheckExplicitUrl;
    private boolean isSecurePortEnabled = false;
    private boolean isUnsecurePortEnabled = true;
    private DataCenterInfo dataCenterInfo;
    private String hostName;
    private boolean isInstanceInfoDirty = false;
    private LeaseInfo leaseInfo;
    private Boolean isCoordinatingDiscoveryServer = Boolean.FALSE;
    private Map<String, String> metadata = new ConcurrentHashMap<String, String>();
    private Long lastUpdatedTimestamp = System.currentTimeMillis();
    private String lastUpdatedTimestampStr ;
    private Long lastDirtyTimestamp = System.currentTimeMillis();
    private String asgName;
    private String version = "unknown";


    private String instanceId;
    private String ip;
    private Integer port;
    private String status;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppGroupName() {
        return appGroupName;
    }

    public void setAppGroupName(String appGroupName) {
        this.appGroupName = appGroupName;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public int getSecurePortT() {
        return securePortT;
    }

    public void setSecurePortT(int securePortT) {
        this.securePortT = securePortT;
    }

    public String getHomePageUrl() {
        return homePageUrl;
    }

    public void setHomePageUrl(String homePageUrl) {
        this.homePageUrl = homePageUrl;
    }

    public String getStatusPageUrl() {
        return statusPageUrl;
    }

    public void setStatusPageUrl(String statusPageUrl) {
        this.statusPageUrl = statusPageUrl;
    }

    public String getHealthCheckUrl() {
        return healthCheckUrl;
    }

    public void setHealthCheckUrl(String healthCheckUrl) {
        this.healthCheckUrl = healthCheckUrl;
    }

    public String getSecureHealthCheckUrl() {
        return secureHealthCheckUrl;
    }

    public void setSecureHealthCheckUrl(String secureHealthCheckUrl) {
        this.secureHealthCheckUrl = secureHealthCheckUrl;
    }

    public String getVipAddress() {
        return vipAddress;
    }

    public void setVipAddress(String vipAddress) {
        this.vipAddress = vipAddress;
    }

    public String getSecureVipAddress() {
        return secureVipAddress;
    }

    public void setSecureVipAddress(String secureVipAddress) {
        this.secureVipAddress = secureVipAddress;
    }

    public String getStatusPageRelativeUrl() {
        return statusPageRelativeUrl;
    }

    public void setStatusPageRelativeUrl(String statusPageRelativeUrl) {
        this.statusPageRelativeUrl = statusPageRelativeUrl;
    }

    public String getStatusPageExplicitUrl() {
        return statusPageExplicitUrl;
    }

    public void setStatusPageExplicitUrl(String statusPageExplicitUrl) {
        this.statusPageExplicitUrl = statusPageExplicitUrl;
    }

    public String getHealthCheckRelativeUrl() {
        return healthCheckRelativeUrl;
    }

    public void setHealthCheckRelativeUrl(String healthCheckRelativeUrl) {
        this.healthCheckRelativeUrl = healthCheckRelativeUrl;
    }

    public String getHealthCheckSecureExplicitUrl() {
        return healthCheckSecureExplicitUrl;
    }

    public void setHealthCheckSecureExplicitUrl(String healthCheckSecureExplicitUrl) {
        this.healthCheckSecureExplicitUrl = healthCheckSecureExplicitUrl;
    }

    public String getVipAddressUnresolved() {
        return vipAddressUnresolved;
    }

    public void setVipAddressUnresolved(String vipAddressUnresolved) {
        this.vipAddressUnresolved = vipAddressUnresolved;
    }

    public String getSecureVipAddressUnresolved() {
        return secureVipAddressUnresolved;
    }

    public void setSecureVipAddressUnresolved(String secureVipAddressUnresolved) {
        this.secureVipAddressUnresolved = secureVipAddressUnresolved;
    }

    public String getHealthCheckExplicitUrl() {
        return healthCheckExplicitUrl;
    }

    public void setHealthCheckExplicitUrl(String healthCheckExplicitUrl) {
        this.healthCheckExplicitUrl = healthCheckExplicitUrl;
    }

    public boolean isSecurePortEnabled() {
        return isSecurePortEnabled;
    }

    public void setSecurePortEnabled(boolean securePortEnabled) {
        isSecurePortEnabled = securePortEnabled;
    }

    public boolean isUnsecurePortEnabled() {
        return isUnsecurePortEnabled;
    }

    public void setUnsecurePortEnabled(boolean unsecurePortEnabled) {
        isUnsecurePortEnabled = unsecurePortEnabled;
    }

    public DataCenterInfo getDataCenterInfo() {
        return dataCenterInfo;
    }

    public void setDataCenterInfo(DataCenterInfo dataCenterInfo) {
        this.dataCenterInfo = dataCenterInfo;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public boolean isInstanceInfoDirty() {
        return isInstanceInfoDirty;
    }

    public void setInstanceInfoDirty(boolean instanceInfoDirty) {
        isInstanceInfoDirty = instanceInfoDirty;
    }

    public LeaseInfo getLeaseInfo() {
        return leaseInfo;
    }

    public void setLeaseInfo(LeaseInfo leaseInfo) {
        this.leaseInfo = leaseInfo;
    }

    public Boolean getCoordinatingDiscoveryServer() {
        return isCoordinatingDiscoveryServer;
    }

    public void setCoordinatingDiscoveryServer(Boolean coordinatingDiscoveryServer) {
        isCoordinatingDiscoveryServer = coordinatingDiscoveryServer;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public Long getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public void setLastUpdatedTimestamp(Long lastUpdatedTimestamp) {
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
    }

    public Long getLastDirtyTimestamp() {
        return lastDirtyTimestamp;
    }

    public void setLastDirtyTimestamp(Long lastDirtyTimestamp) {
        this.lastDirtyTimestamp = lastDirtyTimestamp;
    }

    public String getAsgName() {
        return asgName;
    }

    public void setAsgName(String asgName) {
        this.asgName = asgName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLastUpdatedTimestampStr() {
        return lastUpdatedTimestampStr;
    }

    public void setLastUpdatedTimestampStr(String lastUpdatedTimestampStr) {
        this.lastUpdatedTimestampStr = lastUpdatedTimestampStr;
    }
}
