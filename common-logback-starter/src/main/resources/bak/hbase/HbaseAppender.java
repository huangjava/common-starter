package com.yihu.base.hbase;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.yihu.base.hbase.buffer.HBaseBufferConsumer;
import com.yihu.base.hbase.buffer.HBaseEventBuffer;
import com.yihu.base.hbase.config.HbaseFactory;
import com.yihu.base.hbase.properties.HbaseAppenderProperties;

public class HbaseAppender extends AppenderBase<LoggingEvent> {
    //相关的全部属性
    private HbaseAppenderProperties hbaseAppenderProperties = new HbaseAppenderProperties();
    //消费者
    private Thread bufferConsumerThread = null;
    //缓冲队列
    private HBaseEventBuffer eventBuffer = null;

    private HbaseFactory hbaseFactory = null;


    @Override
    public void start() {
        super.start();
        //初始化内存缓冲队列
        eventBuffer = new HBaseEventBuffer(hbaseAppenderProperties.getBufferProperties());
        //初始化hbase链接
        hbaseFactory = new HbaseFactory(hbaseAppenderProperties.getHbaseProperties());
        hbaseFactory.init();
        //启动消费者
        HBaseBufferConsumer bufferConsumer = new HBaseBufferConsumer(eventBuffer, hbaseAppenderProperties, hbaseFactory);
        bufferConsumerThread = new Thread(bufferConsumer);
        bufferConsumerThread.start();
    }

    @Override
    protected void append(LoggingEvent eventObject) {
        //添加日志到缓冲区
        eventBuffer.addLogEvent(eventObject);
    }


    //========================================properties========================================

    //==============hbase start==============

    public void setTableName(String tableName) {
        hbaseAppenderProperties.getHbaseProperties().setTableName(tableName);
    }

    public void setFamilyName(String familyName) {
        hbaseAppenderProperties.getHbaseProperties().setFamilyName(familyName);
    }

    public void setZkHosts(String zkHosts) {
        hbaseAppenderProperties.getHbaseProperties().setZkHosts(zkHosts);
    }

    public void setZkZnodeParent(String zkZnodeParent) {
        hbaseAppenderProperties.getHbaseProperties().setZkZnodeParent(zkZnodeParent);
    }

    public void setHdfsUserName(String hdfsUserName) {
        hbaseAppenderProperties.getHbaseProperties().setHdfsUserName(hdfsUserName);
    }

    public void setRowkey(String rowkey) {
        hbaseAppenderProperties.getHbaseProperties().setRowkey(rowkey);
    }

    public void setZkPort(String zkPort) {
        hbaseAppenderProperties.getHbaseProperties().setZkPort(zkPort);
    }

    //==============hbase end==============
    //==============buffer start==============
    public void setSleepTime(Long sleepTime) {
        hbaseAppenderProperties.getBufferProperties().setSleepTime(sleepTime);
    }

    public void setBufferSize(Integer bufferSize) {
        hbaseAppenderProperties.getBufferProperties().setBufferSize(bufferSize);
    }


    //==============buffer end==============
}
