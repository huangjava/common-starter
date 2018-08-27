package com.yihu.admin.server.websocket;

import com.yihu.admin.server.websocket.store.EventStore;
import net.sf.json.JSONObject;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by chenweida on 2018/5/22 0022.1
 */
public class WebSocketHandler extends TextWebSocketHandler {
    private static Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    private static final Map<String, WebSocketSession> users;  //Map来存储WebSocketSession，key用USER_ID 即在线用户列表

    //用户标识
    public static final String USER_ID = "WEBSOCKET_USERID";   //对应监听器从的key

    public static final String USER_ADDRESS = "WEBSOCKET_ADDRESS";   //对应监听器从的key

    private EventStore eventStore;


    static {
        users = new HashMap<String, WebSocketSession>();
    }

    public WebSocketHandler(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    /**
     * 连接成功时候，会触发页面上onopen方法
     */
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        logger.info("成功建立websocket连接!");
        String userId = (String) session.getAttributes().get(USER_ID);
        users.put(userId, session);
        logger.info("当前线上监控的服务器数量:" + users.size());

        //这块会实现自己业务，比如，当用户登录后，会把离线消息推送给用户
        //TextMessage returnMessage = new TextMessage("成功建立socket连接，你将收到的离线");
        //session.sendMessage(returnMessage);
    }

    /**
     * 关闭连接时触发
     */
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        logger.debug("关闭websocket连接");
        String userId = (String) session.getAttributes().get(USER_ID);
        logger.info("服务器" + userId + "已退出！");
        users.remove(userId);
        logger.info("剩余监控的服务器" + users.size());
    }

    /**
     * 调用websocket.send时候，会调用该方法
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        /**
         * 收到消息，自定义处理机制，实现业务
         */
        logger.info("服务器收到消息：" + message);
        String messageBody = message.getPayload();
        eventStore.saveEvent(JSONObject.fromObject(messageBody));
    }

    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        logger.info("传输出现异常，关闭websocket连接... ");
        String userId = (String) session.getAttributes().get(USER_ID);
        users.remove(userId);
    }

    public boolean supportsPartialMessages() {
        return false;
    }


    /**
     * 给某个用户发送消息
     *
     * @param userId
     * @param message
     */
    public void sendMessageToUser(String userId, TextMessage message) {
        for (String id : users.keySet()) {
            if (id.equals(userId)) {
                try {
                    if (users.get(id).isOpen()) {
                        users.get(id).sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public void sendMessageToUsers(TextMessage message) {
        for (String userId : users.keySet()) {
            try {
                if (users.get(userId).isOpen()) {
                    users.get(userId).sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取现在的全部用户
     */
    public Map<String, WebSocketSession> getAllUsers() {
        return users;
    }
}
