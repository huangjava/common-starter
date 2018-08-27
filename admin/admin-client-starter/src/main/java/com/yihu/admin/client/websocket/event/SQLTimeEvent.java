package com.yihu.admin.client.websocket.event;

/**
 * Created by chenweida on 2018/5/23 0023.1
 */
public class SQLTimeEvent extends IEvent {
    /**
     * sql
     */
    private String sql;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

}
