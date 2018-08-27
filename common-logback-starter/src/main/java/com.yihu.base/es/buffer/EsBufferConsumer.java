package com.yihu.base.es.buffer;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.yihu.base.es.config.ElasticSearchConnectionFactiory;
import com.yihu.base.es.properties.ElasticsearchAppenderProperties;
import io.searchbox.client.JestClient;
import io.searchbox.core.Bulk;
import io.searchbox.core.BulkResult;
import io.searchbox.core.Index;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenweida on 2018/2/24.
 */
public class EsBufferConsumer implements Runnable {
    //缓冲区
    private EsEventBuffer eventBuffer;
    //消费者相关配政治
    private ElasticsearchAppenderProperties elasticsearchAppenderProperties;
    //格式化日志数据
    private PatternLayout patternLayout = new PatternLayout();

    public EsBufferConsumer(EsEventBuffer eventBuffer, ElasticsearchAppenderProperties elasticsearchAppenderProperties) {
        this.eventBuffer = eventBuffer;
        this.elasticsearchAppenderProperties = elasticsearchAppenderProperties;
    }

    @Override
    public void run() {
        while (true) {
            JestClient jestClient = null;
            try {
                //格式格式化工具
                // patternLayout.setPattern(elasticsearchAppenderProperties.getMessageProperties().getPattern());

                //如果队列没数据休眠
                if (eventBuffer.getBuffer().size() == 0) {
                    sleep();
                    continue;
                }
                List<ILoggingEvent> eventObjectList = new ArrayList<>();
                //获取队列中的全部数据
                eventBuffer.getBuffer().drainTo(eventObjectList);
                //把数据存到ES中
                jestClient = ElasticSearchConnectionFactiory.getConnection();
                Bulk.Builder bulk = new Bulk.Builder()
                        .defaultIndex(elasticsearchAppenderProperties.getElasticsearchProperties().getIndex())
                        .defaultType(elasticsearchAppenderProperties.getElasticsearchProperties().getType());

                for (ILoggingEvent obj : eventObjectList) {
                    Index index = new Index.Builder(obj.getFormattedMessage()).build();
                    bulk.addAction(index);
                }
                BulkResult br = jestClient.execute(bulk.build());

                //线程休眠
                sleep();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (jestClient != null) {
                    jestClient.shutdownClient();
                }
            }
        }
    }

    public void sleep() throws Exception {
        //线程休眠
        Thread.sleep(elasticsearchAppenderProperties.getBufferProperties().getSleepTime());
    }
}
