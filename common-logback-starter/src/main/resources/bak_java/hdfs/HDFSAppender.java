package bak.hdfs;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import bak.hdfs.buffer.HDFSBufferConsumer;
import bak.hdfs.buffer.HDFSEventBuffer;
import bak.hdfs.properties.HDFSAppenderProperties;

/**
 * Created by chenweida on 2018/2/26.
 */
public class HDFSAppender extends AppenderBase<LoggingEvent> {

    private HDFSAppenderProperties hdfsAppenderProperties = new HDFSAppenderProperties();

    private HDFSEventBuffer eventBuffer;
    //消费者
    private Thread bufferConsumerThread = null;
    @Override
    public void start() {
        super.start();
        //初始化内存缓冲队列
        eventBuffer = new HDFSEventBuffer(hdfsAppenderProperties.getBufferProperties());
        //启动消费者
        HDFSBufferConsumer bufferConsumer = new HDFSBufferConsumer(eventBuffer, hdfsAppenderProperties);
        bufferConsumerThread = new Thread(bufferConsumer);
        bufferConsumerThread.start();
    }
    @Override
    protected void append(LoggingEvent eventObject) {

    }


    //========================================properties========================================

    //==============hdfs start==============


    public void setHosts(String hosts) {
        hdfsAppenderProperties.getHdfsProperties().setHosts(hosts);
    }


    public void setPath(String path) {
        hdfsAppenderProperties.getHdfsProperties().setPath(path);
    }

    public void setFileName(String fileName) {
        hdfsAppenderProperties.getHdfsProperties().setFileName(fileName);
    }

    public void setRolling(String rolling) {
        hdfsAppenderProperties.getHdfsProperties().setRolling(rolling);
    }


    public void setSuffix(String suffix) {
        hdfsAppenderProperties.getHdfsProperties().setSuffix(suffix);
    }

    //==============hdfs end==============
    //==============buffer start==============
    public void setSleepTime(Long sleepTime) {
        hdfsAppenderProperties.getBufferProperties().setSleepTime(sleepTime);
    }

    public void setBufferSize(Integer bufferSize) {
        hdfsAppenderProperties.getBufferProperties().setBufferSize(bufferSize);
    }


    //==============buffer end==============
}
