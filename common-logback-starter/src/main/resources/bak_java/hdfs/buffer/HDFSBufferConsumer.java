package bak.hdfs.buffer;

import ch.qos.logback.classic.spi.ILoggingEvent;
import bak.hdfs.properties.HDFSAppenderProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenweida on 2018/2/24.
 */
public class HDFSBufferConsumer implements Runnable {
    //缓冲区
    private HDFSEventBuffer eventBuffer;
    //消费者相关配政治
    private HDFSAppenderProperties hdfsAppenderProperties;

    public HDFSBufferConsumer(HDFSEventBuffer eventBuffer, HDFSAppenderProperties hdfsAppenderProperties) {
        this.eventBuffer = eventBuffer;
        this.hdfsAppenderProperties = hdfsAppenderProperties;
    }

    @Override
    public void run() {
        while (true) {
            try {
                //如果队列没数据休眠
                if (eventBuffer.getBuffer().size() == 0) {
                    sleep();
                    continue;
                }
                List<ILoggingEvent> eventObjectList = new ArrayList<>();
                //获取队列中的全部数据
                eventBuffer.getBuffer().drainTo(eventObjectList);
                //判断hdfs中是否存在这个文件


                //没有就创建hdfs文件并且发送内容

                //有的话就完文件里面追加内容即可

                //线程休眠
                sleep();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sleep() throws Exception {
        //线程休眠
        Thread.sleep(hdfsAppenderProperties.getBufferProperties().getSleepTime());
    }
}
