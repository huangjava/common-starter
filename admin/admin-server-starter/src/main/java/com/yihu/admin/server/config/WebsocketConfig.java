package com.yihu.admin.server.config;

import com.yihu.admin.server.websocket.WebSocketHandler;
import com.yihu.admin.server.websocket.WebSocketHandlerInterceptor;
import com.yihu.admin.server.websocket.store.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Created by chenweida on 2018/5/22 0022.1
 */
@Configuration
@ComponentScan(basePackages = {
        "com.yihu.admin.server.websocket"
})
@EnableWebSocket
public class WebsocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
    @Autowired
    private EventStore eventStore;

    public static final String websocketServer = "/websocket/socketServer";
    public static final String jssocketServer = "/sockjs/socketServer";


    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), websocketServer).addInterceptors(new WebSocketHandlerInterceptor());
        registry.addHandler(webSocketHandler(), jssocketServer).addInterceptors(new WebSocketHandlerInterceptor()).withSockJS();
    }

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new WebSocketHandler(eventStore);
    }

}
