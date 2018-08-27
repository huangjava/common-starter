package com.yihu.admin.client.websocket;

import com.yihu.admin.client.properties.AdminClientProperties;
import com.yihu.admin.client.util.MD5;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenweida on 2018/5/22 0022.1
 */
@Component
public class SocketClient {
    private Logger logger = LoggerFactory.getLogger(SocketClient.class);
    private WebSocketClient client;
    @Autowired
    private AdminClientProperties adminClientProperties;

    @Value("${server.port}")
    private Integer clientPort;

    private Integer retrySize = 3;

    @PostConstruct
    public void init() throws Exception {
        //如果是开启先关闭
        if (client != null && !client.isClosed()) {
            client.close();
        }
        //设置header参数
        InetAddress address = InetAddress.getLocalHost();
        String hostAddress = address.getHostAddress();//192.168.0.121
        String completeAddress = hostAddress + ":" + clientPort;
        Map<String, String> httpHeaders = new HashMap<>();
        httpHeaders.put("address", completeAddress);
        httpHeaders.put("id", MD5.GetMD5Code(completeAddress+new Date().getTime()));


        client = new WebSocketClient(
                new URI("ws://" + adminClientProperties.getIp() + ":" + adminClientProperties.getPort() + "/websocket/socketServer"),
                new Draft_6455(),
                httpHeaders) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                logger.info("connect to server:" + adminClientProperties.getIp() + ":" + adminClientProperties.getPort());
            }

            @Override
            public void onMessage(String s) {
                logger.info("收到消息:" + s);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                logger.warn("websocket is close ");
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                logger.error("websocket error, status:close, message:" + e.getMessage());
            }
        };
        client.connect();
        int retryNum = 1;
        while (retryNum <= retrySize) {
            logger.info("can not connect to server  try :" + retryNum);
            Thread.sleep(3000L);
            retryNum++;
        }

    }

    public WebSocketClient getCurrentClient() {
        return client;
    }
}
