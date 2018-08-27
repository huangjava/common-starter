package com.yihu.base.activemq;

import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.MessageListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by chenweida on 2018/2/11.
 */
public class ActiveMQHelper {


    private Logger logger = LoggerFactory.getLogger(ActiveMQHelper.class);

    private static Map<String, BlockingQueue<DefaultMessageListenerContainer>> holder = new HashMap<String, BlockingQueue<DefaultMessageListenerContainer>>();

    private CachingConnectionFactory cachingConnectionFactory;


    private JmsTemplate jmsTemplate;

    /**
     * 往消息队列发消息
     *
     * @param queueName
     * @param message
     * @return
     * @throws Exception
     */
    public void send(String queueName, Object message) {
        jmsTemplate.convertAndSend(queueName, message);
    }

    /**
     * 动态新增一个监听
     *
     * @param queueName
     * @param messageListener
     * @return
     * @throws Exception
     */
    public synchronized Boolean addListener(String queueName, MessageListener messageListener) {
        try {
            startReceiverByQueueName(messageListener, queueName);
            return true;
        } catch (Exception e) {
            logger.error("新增监听失败:" + e.getMessage());
            return false;
        }
    }

    /**
     * 动态关闭监听
     *
     * @param queueName
     * @return
     */
    public synchronized Boolean closeListener(
            String queueName) {
        try {
            while (true) {
                BlockingQueue<DefaultMessageListenerContainer> defaultMessageListenerContainers = holder.get(queueName);
                if (defaultMessageListenerContainers == null || defaultMessageListenerContainers.size() == 0) {
                    logger.error("关闭失败:消费者不存在或者已经关闭");
                    return false;
                }
                //每次关闭队列头 先进先出
                DefaultMessageListenerContainer defaultMessageListenerContainer = defaultMessageListenerContainers.poll();

                defaultMessageListenerContainer.shutdown();
                if (defaultMessageListenerContainer.isActive() == false) {
                    //如果队列长度是0 那么移除map
                    if (defaultMessageListenerContainers.size() == 0) {
                        holder.remove(queueName);
                    }
                    break;
                }
            }
            return true;
        } catch (Exception e) {
            logger.error("新增监听失败:" + e.getMessage());
            return false;
        }
    }

    private void startReceiverByQueueName(MessageListener receiver, String queueName) throws InterruptedException {
        ActiveMQQueue destination = new ActiveMQQueue(queueName);

        DefaultMessageListenerContainer listenerContainer = new DefaultMessageListenerContainer();
        // 监听容器属性的配置
        listenerContainer.setConnectionFactory(cachingConnectionFactory);
        // 设置目的地
        listenerContainer.setDestination(destination);
        // 设置监听器
        listenerContainer.setMessageListener(receiver);
        // 设置消费者集群数
        int consumers = Integer.valueOf(2);
        listenerContainer.setConcurrentConsumers(consumers);
        // 设置监听队列还是主题 默认是队列
        listenerContainer.setPubSubNoLocal(false);
        // 设置应答模式 默认是4
        listenerContainer.setSessionAcknowledgeMode(ActiveMQSession.INDIVIDUAL_ACKNOWLEDGE);
        // 设置是否启动事物 默认不开启
        listenerContainer.setSessionTransacted(false);
        // 将监听容器保存在holder中
        BlockingQueue basket = holder.get(queueName);
        if (basket == null) {
            basket = new ArrayBlockingQueue(5000);
            basket.put(listenerContainer);
        }
        holder.put(queueName, basket);
        listenerContainer.initialize();
        // 启动监听
        listenerContainer.start();
    }


    public CachingConnectionFactory getCachingConnectionFactory() {
        return cachingConnectionFactory;
    }

    public void setCachingConnectionFactory(CachingConnectionFactory cachingConnectionFactory) {
        this.cachingConnectionFactory = cachingConnectionFactory;
    }

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
}
