package com.yihu.admin.server.log;

/**
 * Created by chenweida on 2018/5/24 0024.1
 */
public class QuotaSQLModel {

    private String sql;
    private Integer excutetime;
    private Long eventstarttime;
    private String eventstarttimestr;

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
}
