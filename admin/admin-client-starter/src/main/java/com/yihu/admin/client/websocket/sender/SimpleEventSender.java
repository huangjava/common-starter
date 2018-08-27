package com.yihu.admin.client.websocket.sender;

import com.yihu.admin.client.websocket.SocketClient;
import com.yihu.admin.client.websocket.buffer.EventBuffer;
import com.yihu.admin.client.websocket.event.IEvent;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chenweida on 2018/5/23 0023.1
 */
@Component
public class SimpleEventSender implements EventSender {
    private Logger logger = LoggerFactory.getLogger(SimpleEventSender.class);
    @Autowired
    private Tracer tracer;
    @Value("${spring.application.name:unknow}")
    private String spanrName;

    @Autowired
    private EventBuffer eventBuffer;
    @Autowired
    private SocketClient socketClient;

    public Boolean send(IEvent event) {
        try {
            if (StringUtils.isEmpty(event.getTraceId())) {
                event.setTraceId(tracer.getCurrentSpan().traceIdString());
            }
            if (StringUtils.isEmpty(event.getSpanId())) {
                event.setSpanId(tracer.getCurrentSpan().getSpanId() + "");
            }
            if (StringUtils.isEmpty(event.getSpanName())) {
                event.setSpanName(spanrName);
            }
            JSONObject jsonObject = new JSONObject();
            Method[] methods = event.getClass().getMethods();
            for (Method method : methods) {
                if ("getclass".equals(method.getName().toLowerCase())) {
                    continue;
                }
                if (method.getName().startsWith("get")) {
                    String methodName = method.getName();
                    String keyName = methodName.replace("get", "").toLowerCase();
                    Object value = method.invoke(event);
                    if (value != null) {
                        if (value instanceof Date) {
                            Date valueDate = (Date) value;
                            jsonObject.put(keyName, valueDate.getTime());
                        } else if (value instanceof java.sql.Date) {
                            java.sql.Date valueDate = (java.sql.Date) value;
                            jsonObject.put(keyName, valueDate.getTime());
                        } else {
                            jsonObject.put(keyName, value);
                        }

                    }
                }
            }
            socketClient.getCurrentClient().send(JSONObject.fromObject(event).toString());
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Scheduled(cron = "0/10 * * * * ?")
    public void send() {
        try {
            int success = 0;
            int error = 0;
            //如果队列没数据休眠
            if (eventBuffer.getBuffer().size() == 0) {
                return;
            }
            List<IEvent> eventObjectList = new ArrayList<>();
            //获取队列中的全部数据
            eventBuffer.getBuffer().drainTo(eventObjectList);
            for (IEvent event : eventObjectList) {
                if (send(event)) {
                    success++;
                } else {
                    error++;
                }
            }
            logger.info(new StringBuffer("all:" + eventObjectList.size() + ",success:" + success + ",error:" + error).toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }
    }
}
