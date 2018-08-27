package com.yihu.admin.client.websocket;

import org.java_websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by chenweida on 2018/5/22 0022.1
 */
@Component
public class SocketHeart {
    private Logger logger = LoggerFactory.getLogger(SocketHeart.class);
    @Autowired
    private SocketClient socketClient;

    @Scheduled(cron = "0/30 * * * * ?")
    public void TaskJob() {
        try {
            WebSocketClient webSocketClient = socketClient.getCurrentClient();
            if (webSocketClient == null || webSocketClient.isClosed()) {
                try {
                    webSocketClient.close();
                } finally {
                    logger.warn("restart connect to server !!");
                    socketClient.init();
                    if (!socketClient.getCurrentClient().isClosed()) {
                        logger.warn("init SocketClient success !!");
                    }else{
                        logger.warn("init SocketClient fail !!");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
