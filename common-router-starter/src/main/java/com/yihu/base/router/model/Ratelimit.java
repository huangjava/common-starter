package com.yihu.base.router.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Created by chenweida on 2018/4/27 0027.
 */
public class Ratelimit {
    private String id;//主键
    private String includeIp;//ip地址 为空默认是全部的ip都可以访问
    private String excludeIp;//ip地址 为空默认是全部的ip都可以访问
    private String path;// 限制访问的路径
    private Integer accessCount = 0;//次数 默认是0 表示没有限制
    private String accessUnit = "2";//访问的次数限制单位 默认是分钟  1秒 2 分钟 3小时 4天 结合accessCount使用

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIncludeIp() {
        return includeIp;
    }

    public void setIncludeIp(String includeIp) {
        this.includeIp = includeIp;
    }

    public String getExcludeIp() {
        return excludeIp;
    }

    public void setExcludeIp(String excludeIp) {
        this.excludeIp = excludeIp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(Integer accessCount) {
        this.accessCount = accessCount;
    }

    public String getAccessUnit() {
        return accessUnit;
    }

    public void setAccessUnit(String accessUnit) {
        this.accessUnit = accessUnit;
    }

    public String getEndTime() {
        switch (accessUnit) {
            case "1": {
                return LocalDateTime.now().minusSeconds(-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            case "2": {
                return LocalDateTime.now().minusMinutes(-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"+":00"));
            }
            case "3": {
                return LocalDateTime.now().minusHours(-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH"+":00:00"));
            }
            case "4": {
                return LocalDateTime.now().minusDays(-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"+" 00:00:00"));
            }
        }
        return null;
    }

}
