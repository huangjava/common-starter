package com.yihu.admin.server.websocket;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;


/**
 * WebSocket拦截器----握手之前将登陆用户信息从session设置到WebSocketSession1
 */
public class WebSocketHandlerInterceptor extends HttpSessionHandshakeInterceptor {
    private Logger logger = LoggerFactory.getLogger(WebSocketHandlerInterceptor.class);

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;

            //使用userName区分WebSocketHandler，以便定向发送消息
            String id = servletRequest.getHeaders().get("id").toString();  //一般直接保存user实体
            String address = servletRequest.getHeaders().get("address").toString();  //一般直接保存user实体
            logger.info("注入到服务列表的userName：" + id);
            if (!StringUtils.isEmpty(id)) {
                attributes.put(com.yihu.admin.server.websocket.WebSocketHandler.USER_ID, id);
            }
            if (!StringUtils.isEmpty(address)) {
                attributes.put(com.yihu.admin.server.websocket.WebSocketHandler.USER_ADDRESS, address);
            }
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);

    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
