package com.yihu.admin.server.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Created by chenweida on 2018/5/22 0022.1
 */
@RestController
@RequestMapping("socketServer")
public class SocketEndPoint {
    @Autowired
    private WebSocketHandler webSocketHandler;

    @PostMapping("sendTest")
    public void sendTest(
            @RequestParam(value = "userid", required = true) String userid,
            @RequestParam(value = "message", required = true) String message
    ) {
        webSocketHandler.getAllUsers();
    }

    @GetMapping("getAllUsers")
    public String getAllUsers(
    ) {
        return webSocketHandler.getAllUsers().keySet().toString();
    }
}
