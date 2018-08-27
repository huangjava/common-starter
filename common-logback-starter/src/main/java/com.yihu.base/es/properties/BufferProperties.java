package com.yihu.base.es.properties;

/**
 * Created by chenweida on 2018/2/24.
 * 缓冲区相关配置
 */
public class BufferProperties {

    private Long sleepTime = 1000L;//多久消费一次消息
    private Integer bufferSize = 100000;//缓冲区的大小

    public Long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(Long sleepTime) {
        this.sleepTime = sleepTime;
    }

    public Integer getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(Integer bufferSize) {
        this.bufferSize = bufferSize;
    }
}
