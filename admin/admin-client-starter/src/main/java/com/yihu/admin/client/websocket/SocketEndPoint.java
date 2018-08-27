package com.yihu.admin.client.websocket;

import com.yihu.admin.client.properties.AdminClientProperties;
import com.yihu.admin.client.websocket.event.SQLTimeEvent;
import com.yihu.admin.client.websocket.sender.SimpleEventSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by chenweida on 2018/5/22 0022.1
 */
@RestController
@RequestMapping("socketClient")
public class SocketEndPoint {

    @Autowired
    private SocketClient socketClient;
    private Logger logger = LoggerFactory.getLogger(SocketEndPoint.class);
    @Autowired
    private SimpleEventSender simpleEventSender;



    @PostMapping("sendTest")
    public String sendTest(
            @RequestParam(value = "message", required = true) String message
    ) {
        if (socketClient.getCurrentClient().isClosed()) {
            return "socket 已经关闭";
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        SQLTimeEvent sqlTimeEvent = new SQLTimeEvent();
        sqlTimeEvent.setEventName("测试");
        sqlTimeEvent.setSql(message);
        simpleEventSender.send(sqlTimeEvent);
        return "发送成功";
    }

    @PostMapping("initAgent")
    public String initAgent(
    ) {
        try {
            socketClient.init();
            return "启动成功";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
