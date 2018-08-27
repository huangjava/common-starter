package com.yihu.base.hbase.buffer;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.alibaba.fastjson.JSON;
import com.yihu.base.hbase.config.HbaseFactory;
import com.yihu.base.hbase.properties.HbaseAppenderProperties;
import com.yihu.base.hbase.properties.HbaseProperties;
import com.yihu.base.hbase.rowkey.RowkeyFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenweida on 2018/2/24.
 */
public class HBaseBufferConsumer implements Runnable {
    //缓冲区
    private HBaseEventBuffer eventBuffer;
    //消费者相关配置
    private HbaseAppenderProperties hbaseAppenderProperties;
    //消费者相关配置
    private HbaseProperties hbaseProperties;

    private HbaseFactory hbaseFactory=null;

    public HBaseBufferConsumer(HBaseEventBuffer eventBuffer, HbaseAppenderProperties hbaseAppenderProperties,HbaseFactory hbaseFactory) {
        this.eventBuffer = eventBuffer;
        this.hbaseAppenderProperties = hbaseAppenderProperties;
        this.hbaseFactory=hbaseFactory;
        this.hbaseProperties=hbaseAppenderProperties.getHbaseProperties();
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

                List<String> rowkeList=new ArrayList<>();
                List<Map<String,Map<String,String>>> familyList=new ArrayList<>();

                for(ILoggingEvent loggingEvent:eventObjectList){
                    rowkeList.add(RowkeyFactory.getRowkey(hbaseProperties));

                    Map<String,String> logMap = (Map<String, String>) JSON.parse(loggingEvent.getFormattedMessage());

                    Map<String,Map<String,String>> data =new HashMap<>();
                    data.put(hbaseProperties.getFamilyName(),logMap);
                    familyList.add(data);
                }


                hbaseFactory.addLogBulk(hbaseProperties.getTableName(),rowkeList,familyList);
                //线程休眠
                sleep();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sleep() throws Exception {
        //线程休眠
        Thread.sleep(hbaseAppenderProperties.getBufferProperties().getSleepTime());
    }
}
