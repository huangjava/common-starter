package com.yihu.admin.client.advice;

import com.yihu.admin.client.properties.AdminClientProperties;
import com.yihu.admin.client.websocket.buffer.EventBuffer;
import com.yihu.admin.client.websocket.event.SQLTimeEvent;
import org.hibernate.EmptyInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Iterator;

/**
 * Created by chenweida on 2018/5/31 0031.
 */
@Component
@Order(0)
@ConditionalOnProperty(name = "yihu.admin.client.advice.sql.hibernate", havingValue = "true")
public class HibernateFilter extends EmptyInterceptor {

    private Logger logger = LoggerFactory.getLogger(HibernateFilter.class);
    @Autowired
    private Tracer tracer;
    @Autowired
    private EventBuffer eventBuffer;
    @Autowired
    private AdminClientProperties adminClientProperties;
    @Value("${spring.application.name:unknow}")
    private String spanrName;


    public HibernateFilter() {
        logger.info(" init HibernateFilter success!!!");
    }

    @Override
    public String onPrepareStatement(String sql) {
        if (adminClientProperties.getHibernateSql()) {
            Long endTime = new Date().getTime();
            SQLTimeEvent event = new SQLTimeEvent();
            //用改变后的参数执行目标方法
            event.setSql(sql);
            event.setEventStartTime(endTime);
            event.setEventEndTime(endTime);
            event.setEventName("system_sql_hibernate_tracer");
            event.setExcuteTime(-1L);
            event.setSpanName(spanrName);
            event.setSpanId(tracer.getCurrentSpan().getSpanId() + "");
            event.setTraceId(tracer.getCurrentSpan().traceIdString());
            event.setSuccess(1);
            eventBuffer.addEvent(event);
        }
        return sql;
    }

}