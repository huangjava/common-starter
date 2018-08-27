package com.yihu.admin.client.config;

import com.yihu.admin.client.advice.HibernateFilter;
import com.yihu.admin.client.properties.AdminClientProperties;
import com.yihu.admin.client.websocket.SocketClient;
import com.yihu.admin.client.websocket.buffer.EventBuffer;
import com.yihu.admin.client.websocket.sender.SimpleEventSender;
import org.hibernate.EmptyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenweida on 2018/5/22 0022.1
 */
@ComponentScan(basePackages = {
        "com.yihu.admin.client.websocket",
        "com.yihu.admin.client.advice",
        "com.yihu.admin.client.properties",
        "com.yihu.admin.client.util"
})
@Configuration
public class WebsocketConfig {
    @Autowired
    SocketClient socketClient;
    @Autowired
    AdminClientProperties adminClientProperties;

    @Bean
    public EventBuffer eventBuffer() {
        return new EventBuffer(adminClientProperties);
    }


}