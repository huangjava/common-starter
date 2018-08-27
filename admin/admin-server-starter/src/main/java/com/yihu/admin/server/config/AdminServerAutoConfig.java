package com.yihu.admin.server.config;

import com.yihu.admin.server.properties.AdminServerProperties;
import com.yihu.admin.server.websocket.store.ElasticSearchStore;
import com.yihu.admin.server.websocket.store.EsEventBuffer;
import com.yihu.admin.server.websocket.store.EventStore;
import com.yihu.base.es.config.ElastricSearchHelper;
import org.elasticsearch.action.admin.indices.alias.exists.AliasesExistResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Created by chenweida on 2018/5/15 0015.1
 */
@ComponentScan(basePackages = {
        "com.yihu.admin.server.eureka",
        "com.yihu.admin.server.manage",
        "com.yihu.admin.server.properties",
        "com.yihu.admin.server.log"
})
@Configuration
public class AdminServerAutoConfig {

    private Logger logger = LoggerFactory.getLogger(AdminServerAutoConfig.class);
    @Autowired
    private ElastricSearchHelper elastricSearchHelper;
    @Autowired
    private AdminServerProperties adminServerProperties;
    @Autowired
    private EsEventBuffer esEventBuffer;

    @Bean
    @ConditionalOnMissingClass
    public EventStore eventStore() {
        ElasticSearchStore elasticSearchStore = new ElasticSearchStore();
        elasticSearchStore.setEsEventBuffer(esEventBuffer);
        return elasticSearchStore;
    }

    @PostConstruct
    public void createIndex() throws Exception {
        Client client = elastricSearchHelper.getElasticsearchUtil().getElasticFactory();
        try {
            //判断index是否存在
            Boolean flag = client.prepareExists(adminServerProperties.getRealDefaultIndexName()).get().exists();
            if (!flag) {
                createIndex(client);
            }
        } catch (org.elasticsearch.index.IndexNotFoundException e) {
            createIndex(client);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void createIndex(Client client) {
        try {
            CreateIndexRequestBuilder cib = client.admin()
                    .indices().prepareCreate(adminServerProperties.getRealDefaultIndexName());
            XContentBuilder mapping = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject("properties") //设置之定义字段
                    .startObject("uri")
                    .field("type", "string") //设置数据类型
                    .field("index", "not_analyzed") //设置数据类型	"index": "not_analyzed"
                    .endObject()
                    .startObject("spanname")
                    .field("type", "string")
                    .field("index", "not_analyzed") //设置数据类型	"index": "not_analyzed"
                    .endObject()
                    .startObject("ip")
                    .field("type", "string")
                    .field("index", "not_analyzed") //设置数据类型	"index": "not_analyzed"
                    .endObject()
                    .startObject("eventname")
                    .field("type", "string")
                    .field("index", "not_analyzed") //设置数据类型	"index": "not_analyzed"
                    .endObject()
                    .startObject("method")
                    .field("type", "string")
                    .field("index", "not_analyzed") //设置数据类型	"index": "not_analyzed"
                    .endObject()
                    .endObject()
                    .endObject();
            cib.addMapping(adminServerProperties.getDefaultTypeName(), mapping);
            CreateIndexResponse indexResponse = cib.execute().actionGet();
            logger.info("自动创建索引");
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }
}
