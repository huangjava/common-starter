package com.yihu.admin.client.advice;

import com.yihu.admin.client.properties.AdminClientProperties;
import com.yihu.admin.client.websocket.buffer.EventBuffer;
import com.yihu.admin.client.websocket.event.SQLTimeEvent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenweida on 2018/5/21 0021.1
 * 数据库sql的拦截
 */
@Aspect
@Component
@ConditionalOnProperty(name = "yihu.admin.client.advice.sql.springJDBC", havingValue = "true")
public class SQLTimeAdvice {
    @Autowired
    private Tracer tracer;
    @Autowired
    private EventBuffer eventBuffer;
    @Autowired
    private AdminClientProperties adminClientProperties;

    private Logger logger = LoggerFactory.getLogger(SQLTimeAdvice.class);
    @Value("${spring.application.name:unknow}")
    private String spanrName;


    @Around("execution(* org.springframework.jdbc.core.JdbcTemplate.batchUpdate*(..)) " +
            "|| execution(* org.springframework.jdbc.core.JdbcTemplate.query*(..)) " +
            "|| execution(* org.springframework.jdbc.core.JdbcTemplate.execute*(..)) " )
    public Object process(ProceedingJoinPoint point) throws Throwable {
        if (!adminClientProperties.getSql()) {
            return point.proceed();
        }
        SQLTimeEvent event = new SQLTimeEvent();
        HttpServletRequest request = null;
        //访问目标方法的参数：
        Object[] args = point.getArgs();
        String sql = null;
        //第一个参数是sql
        if (args != null && args.length > 0 && args[0].getClass() == String.class) {
            sql = args[0].toString();
            if (sql.contains("from")) {
                event.setSql(sql);
                logger.debug("sql:" + sql);
            }
            //第一个参数是sql数组
        }
        //用改变后的参数执行目标方法
        Long startTime = System.currentTimeMillis();
        event.setEventStartTime(startTime);
        try {
            Object returnValue = point.proceed(args);
            Long endTime = System.currentTimeMillis();
            event.setEventEndTime(endTime);
            event.setEventName("system_sql_springJDBC_tracer");
            event.setExcuteTime(endTime - startTime);
            event.setSpanName(spanrName);
            event.setSpanId(tracer.getCurrentSpan().getSpanId() + "");
            event.setTraceId(tracer.getCurrentSpan().traceIdString());
            event.setSuccess(1);
            eventBuffer.addEvent(event);
            logger.debug("event:" + event.toString());
            return returnValue;
        } catch (Exception e) {
            Long endTime = System.currentTimeMillis();
            event.setEventEndTime(endTime);
            event.setEventName("system_sql_springJDBC_tracer");
            event.setExcuteTime(endTime - startTime);
            event.setSpanName(spanrName);
            event.setSpanId(tracer.getCurrentSpan().getSpanId() + "");
            event.setTraceId(tracer.getCurrentSpan().traceIdString());
            event.setFail(1);
            eventBuffer.addEvent(event);
            throw new Exception(e);
        }

    }
}