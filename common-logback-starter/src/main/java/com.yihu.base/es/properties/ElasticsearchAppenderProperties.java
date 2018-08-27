package com.yihu.base.es.properties;

/**
 * Created by chenweida on 2018/2/24.
 */
public class ElasticsearchAppenderProperties {
    //es相关的配置
    private ElasticsearchProperties elasticsearchProperties = new ElasticsearchProperties();
    //缓存区相关的配置
    private BufferProperties bufferProperties = new BufferProperties();
    //日志相关配置
    private MessageProperties messageProperties = new MessageProperties();


    public ElasticsearchProperties getElasticsearchProperties() {
        return elasticsearchProperties;
    }

    public void setElasticsearchProperties(ElasticsearchProperties elasticsearchProperties) {
        this.elasticsearchProperties = elasticsearchProperties;
    }

    public BufferProperties getBufferProperties() {
        return bufferProperties;
    }

    public void setBufferProperties(BufferProperties bufferProperties) {
        this.bufferProperties = bufferProperties;
    }

    public MessageProperties getMessageProperties() {
        return messageProperties;
    }

    public void setMessageProperties(MessageProperties messageProperties) {
        this.messageProperties = messageProperties;
    }
}
