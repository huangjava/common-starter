package com.yihu.base.es.config;

import com.yihu.base.es.properties.ElasticsearchProperties;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenweida on 2018/2/24.
 * es链接工程
 */
public class ElasticSearchConnectionFactiory {
    private static JestClientFactory factory = null;

    private ElasticSearchConnectionFactiory() {
    }

    /**
     * 初始化链接
     *
     * @param elasticsearchProperties
     */
    synchronized static public void init(ElasticsearchProperties elasticsearchProperties) {
        if (factory == null) {
            String[] hostArray = elasticsearchProperties.getHosts().split(",");
            factory = new JestClientFactory();
            HttpClientConfig httpClientConfig = new HttpClientConfig
                    .Builder(Arrays.asList(hostArray))
                    .multiThreaded(elasticsearchProperties.getMultiThreaded())
                    .maxTotalConnection(elasticsearchProperties.getMaxTotalConnection())// 最大链接
                    .maxConnectionIdleTime(elasticsearchProperties.getMaxConnectionIdleTime(), TimeUnit.MINUTES)//链接等待时间
                    .connTimeout(elasticsearchProperties.getConnTimeout())
                    .discoveryEnabled(elasticsearchProperties.getDiscoveryEnabled())
                    .readTimeout(elasticsearchProperties.getReadTimeout())//60秒
                    .build();
            factory.setHttpClientConfig(httpClientConfig);//得到链接
        }
    }

    /**
     * 获取链接
     *
     * @return
     */
    public static JestClient getConnection() {

        return factory.getObject();
    }
}
