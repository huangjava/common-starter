package com.yihu.admin.server.log;

import com.yihu.admin.server.util.DateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by chenweida on 2018/5/24 0024.1
 */
public class LogModel {
    private String sql;
    private String spanid;
    private String spanname;
    private String eventname;
    private String traceid;
    private String method;
    private String ip;
    private String uri;
    private String requestparams;
    private String responseparams;
    private Long eventstarttime;
    private String eventstarttimestr;
    private Long eventendtime;
    private String eventendtimestr;
    private Integer excutetime;
    private String header;


    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Integer getExcutetime() {
        return excutetime;
    }

    public void setExcutetime(Integer excutetime) {
        this.excutetime = excutetime;
    }

    public String getSpanid() {
        return spanid;
    }

    public void setSpanid(String spanid) {
        this.spanid = spanid;
    }

    public String getSpanname() {
        return spanname;
    }

    public void setSpanname(String spanname) {
        this.spanname = spanname;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getTraceid() {
        return traceid;
    }

    public void setTraceid(String traceid) {
        this.traceid = traceid;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Long getEventstarttime() {
        return eventstarttime;
    }

    public void setEventstarttime(Long eventstarttime) {
        this.eventstarttime = eventstarttime;
    }

    public String getEventstarttimestr() {
        return eventstarttimestr;
    }

    public void setEventstarttimestr(String eventstarttimestr) {
        this.eventstarttimestr = eventstarttimestr;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getResponseparams() {
        return responseparams;
    }

    public void setResponseparams(String responseparams) {
        this.responseparams = responseparams;
    }

    public Long getEventendtime() {
        return eventendtime;
    }

    public void setEventendtime(Long eventendtime) {
        this.eventendtime = eventendtime;
    }

    public String getEventendtimestr() {
        return eventendtimestr;
    }

    public void setEventendtimestr(String eventendtimestr) {
        this.eventendtimestr = eventendtimestr;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRequestparams() {
        return requestparams;
    }

    public void setRequestparams(String requestparams) {
        this.requestparams = requestparams;
    }
}
