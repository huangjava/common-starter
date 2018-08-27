package com.yihu.base.activemq.config.jms;


import com.yihu.base.activemq.ActiveMQHelper;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

/**
 * Created by chenweida on 2018/1/23.
 */
@Configuration
@EnableJms
public class JmsActivcemqConfig {
    @Autowired
    private ActiveMQProperties activeMQProperties;

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(activeMQProperties.getBrokerUrl());
        connectionFactory.setUserName(activeMQProperties.getUser());
        connectionFactory.setPassword(activeMQProperties.getPassword());
        //设置异步发送
        connectionFactory.setUseAsyncSend(true);
        return connectionFactory;

    }

    /**
     * 缓存session链接
     *
     * @return
     */
    @Bean
    @Primary
    public CachingConnectionFactory CachingConnectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        //目标ConnectionFactory对应真实的可以产生JMS Connection的ConnectionFactory
        cachingConnectionFactory.setTargetConnectionFactory(connectionFactory());
        //Session缓存数量,这里属性也可以直接在这里配置
        cachingConnectionFactory.setSessionCacheSize(100);
        return cachingConnectionFactory;
    }

    @Bean
    @Primary
    public JmsTemplate jmsQueueTemplate() {
        return new JmsTemplate(connectionFactory());

    }


    @Bean
    @Primary
    public ActiveMQHelper actifveMQHelper() {
        ActiveMQHelper actifveMQHelper = new ActiveMQHelper();
        actifveMQHelper.setCachingConnectionFactory(CachingConnectionFactory());
        actifveMQHelper.setJmsTemplate(jmsQueueTemplate());
        return actifveMQHelper;
    }

}
