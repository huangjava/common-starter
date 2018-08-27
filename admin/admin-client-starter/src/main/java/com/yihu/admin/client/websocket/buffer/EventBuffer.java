package com.yihu.admin.client.websocket.buffer;

import ch.qos.logback.classic.spi.LoggingEvent;
import com.yihu.admin.client.properties.AdminClientProperties;
import com.yihu.admin.client.websocket.event.IEvent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by chenweida on 2018/2/24.
 */
public class EventBuffer {
    //缓冲队列
    BlockingQueue queue = null;

    public EventBuffer(AdminClientProperties adminClientProperties) {
        queue = new ArrayBlockingQueue(adminClientProperties.getBufferSize());
    }

    public BlockingQueue getBuffer() {
        return queue;
    }


    public void addEvent(IEvent event) {
        queue.add(event);
    }
}
