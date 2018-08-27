package com.yihu.base.es;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.yihu.base.es.buffer.EsBufferConsumer;
import com.yihu.base.es.buffer.EsEventBuffer;
import com.yihu.base.es.config.ElasticSearchConnectionFactiory;
import com.yihu.base.es.properties.ElasticsearchAppenderProperties;

public class ElasticsearchAppender extends AppenderBase<LoggingEvent> {
    //相关的全部属性
    private ElasticsearchAppenderProperties elasticsearchAppenderProperties = new ElasticsearchAppenderProperties();
    //消费者
    private Thread bufferConsumerThread = null;
    //缓冲队列
    private EsEventBuffer eventBuffer = null;

    public ElasticsearchAppender() {
    }


    @Override
    public void start() {
        super.start();
        //初始化内存缓冲队列
        eventBuffer = new EsEventBuffer(elasticsearchAppenderProperties.getBufferProperties());
        //初始化ES连接池
        ElasticSearchConnectionFactiory.init(elasticsearchAppenderProperties.getElasticsearchProperties());
        //启动消费者
        EsBufferConsumer bufferConsumer = new EsBufferConsumer(eventBuffer, elasticsearchAppenderProperties);
        bufferConsumerThread = new Thread(bufferConsumer);
        bufferConsumerThread.start();
    }

    @Override
    protected void append(LoggingEvent eventObject) {
        //添加日志到缓冲区
        eventBuffer.addLogEvent(eventObject);
    }


    //========================================properties========================================

    //==============elasticsearch start==============
    public void setHosts(String hosts) {
        elasticsearchAppenderProperties.getElasticsearchProperties().setHosts(hosts);
    }

    public void setIndex(String index) {

        elasticsearchAppenderProperties.getElasticsearchProperties().setIndex(index);
    }

    public void setType(String type) {
        elasticsearchAppenderProperties.getElasticsearchProperties().setType(type);
    }

    public void setClusterName(String clusterName) {
        elasticsearchAppenderProperties.getElasticsearchProperties().setClusterName(clusterName);
    }

    public void setMaxTotalConnection(Integer maxTotalConnection) {
        elasticsearchAppenderProperties.getElasticsearchProperties().setMaxTotalConnection(maxTotalConnection);
    }

    public void setMaxConnectionIdleTime(Integer maxConnectionIdleTime) {
        elasticsearchAppenderProperties.getElasticsearchProperties().setMaxConnectionIdleTime(maxConnectionIdleTime);
    }

    public void setRolling(String rolling) {
        elasticsearchAppenderProperties.getElasticsearchProperties().setRolling(rolling);
    }

    public void setConnTimeout(Integer connTimeout) {
        elasticsearchAppenderProperties.getElasticsearchProperties().setConnTimeout(connTimeout);
    }

    public void setReadTimeout(Integer readTimeout) {
        elasticsearchAppenderProperties.getElasticsearchProperties().setReadTimeout(readTimeout);
    }


    public void setMultiThreaded(Boolean multiThreaded) {
        elasticsearchAppenderProperties.getElasticsearchProperties().setMultiThreaded(multiThreaded);
    }

    public void setDiscoveryEnabled(Boolean discoveryEnabled) {
        elasticsearchAppenderProperties.getElasticsearchProperties().setDiscoveryEnabled(discoveryEnabled);
    }

    //==============elasticsearch end==============
    //==============buffer start==============
    public void setSleepTime(Long sleepTime) {
        elasticsearchAppenderProperties.getBufferProperties().setSleepTime(sleepTime);
    }

    public void setBufferSize(Integer bufferSize) {
        elasticsearchAppenderProperties.getBufferProperties().setBufferSize(bufferSize);
    }


    //==============buffer end==============
    //==============message start==============
    public void setPattern(String pattern) {
        elasticsearchAppenderProperties.getMessageProperties().setPattern(pattern);
    }

    //==============message end==============
}
