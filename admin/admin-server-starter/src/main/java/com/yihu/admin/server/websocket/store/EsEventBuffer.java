package com.yihu.admin.server.websocket.store;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import com.yihu.admin.server.properties.AdminServerProperties;
import com.yihu.base.es.config.ElastricSearchHelper;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by chenweida on 2018/2/24.1
 */
@Component
public class EsEventBuffer {
    //缓冲队列
    BlockingQueue queue = null;
    @Autowired
    private ElastricSearchHelper elastricSearchHelper;
    @Autowired
    private AdminServerProperties adminServerProperties;

    public EsEventBuffer() {
        queue = new ArrayBlockingQueue(50000);
    }

    public BlockingQueue getBuffer() {
        return queue;
    }

    public void setQueue(BlockingQueue queue) {
        this.queue = queue;
    }

    public void addLogEvent(String eventObject) {
        queue.add(eventObject);
    }


    @Scheduled(cron = "0/3 * * * * ?")
    public void TaskJob() {
        try {
            List<Object> eventObjectList = new ArrayList<>();
            //获取队列中的全部数据
            queue.drainTo(eventObjectList);
            if (eventObjectList.size() > 0) {
                elastricSearchHelper.save(adminServerProperties.getRealDefaultIndexName(), adminServerProperties.getDefaultTypeName(), eventObjectList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
