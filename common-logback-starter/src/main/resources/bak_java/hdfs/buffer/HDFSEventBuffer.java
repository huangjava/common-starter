package bak.hdfs.buffer;

import ch.qos.logback.classic.spi.LoggingEvent;
import bak.hdfs.properties.BufferProperties;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by chenweida on 2018/2/24.
 */
public class HDFSEventBuffer {
    //缓冲队列
    BlockingQueue queue = null;

    public HDFSEventBuffer(BufferProperties bufferProperties) {
        queue = new ArrayBlockingQueue(bufferProperties.getBufferSize());
    }

    public BlockingQueue getBuffer() {
        return queue;
    }

    public void setQueue(BlockingQueue queue) {
        this.queue = queue;
    }

    public void addLogEvent(LoggingEvent eventObject) {
        queue.add(eventObject);
    }
}
