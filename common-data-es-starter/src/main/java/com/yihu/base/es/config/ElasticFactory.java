package com.yihu.base.es.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.ElasticSearchDruidDataSourceFactory;
import com.yihu.base.es.config.properties.ElasticSearchPorperties;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenweida on 2017/6/5.
 */
@Configuration
public class ElasticFactory {
    private static JestClientFactory factory = null;
    @Autowired
    private ElasticSearchPorperties elasticSearchPorperties;
//-----------------------------------jestClient----------------------------------------

    /**
     * @param "http://localhost:9200"
     * @return
     */
    public JestClient getJestClient() {
        if (factory == null) {
            //初始化链接
            init();
        }
        return factory.getObject();
    }

    /**
     * 初始化链接
     * 9200
     */
    @PostConstruct
    public void initAll() {
        try {
            init();
            initTranClient();
        } catch (UnknownHostException e) {

        }
    }

    public synchronized void init() {
        String[] hostArray = elasticSearchPorperties.getClusterNodesJest().split(",");
        // Construct a new Jest client according to configuration via factory
        factory = new JestClientFactory();
        HttpClientConfig httpClientConfig = new HttpClientConfig
                .Builder(Arrays.asList(hostArray))//http://59.61.92.90:9065,http://59.61.92.90:9067
                .multiThreaded(elasticSearchPorperties.getJestMultiThreaded())
                .maxTotalConnection(elasticSearchPorperties.getJestMaxTotalConnection())// 最大链接
                .maxConnectionIdleTime(elasticSearchPorperties.getJestMaxConnectionIdleTime(), TimeUnit.SECONDS)//链接等待时间
                .connTimeout(elasticSearchPorperties.getJestConnTimeout())
                .discoveryEnabled(elasticSearchPorperties.getJestDiscoveryEnabled())
                .readTimeout(elasticSearchPorperties.getJestReadTimeout())//60秒
                .build();


        factory.setHttpClientConfig(httpClientConfig);//得到链接
    }

    //-----------------------------------TransportClient----------------------------------------

    private TransportClient transportClient;

    public Client getTransportClient() {
        try {
            initTranClient();
            return transportClient;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 9300
     *
     * @throws UnknownHostException
     */
    private synchronized void initTranClient() throws UnknownHostException {
        if (transportClient == null) {
            String[] hosts = elasticSearchPorperties.getClusterNodes().split(",");
            Settings settings = Settings.settingsBuilder()
                    .put("client.transport.sniff", elasticSearchPorperties.getJestDiscoveryEnabled())//开启嗅探功能
                    .put("cluster.name", StringUtils.isEmpty(elasticSearchPorperties.getClusterName()) ? "jkzl" : elasticSearchPorperties.getClusterName())//默认集群名字是jkzl
                    .build();

            transportClient = TransportClient.builder().settings(settings).build();

            for (String oneHost : hosts) {
                String[] hostAndport = oneHost.split(":");
                transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostAndport[0]), Integer.valueOf(hostAndport[1])));
            }
        }
    }

    /**
     * es连接池
     *
     * @return
     * @throws Exception
     */
    @Bean
    public DruidDataSource esDruidDataSource() {
        DruidDataSource druidDataSource = null;
        try {
            Properties properties = new Properties();
            properties.put("url", "jdbc:elasticsearch://" + elasticSearchPorperties.getClusterNodes().split(",")[0] + "/wlyy_quota_test");
            druidDataSource = (DruidDataSource) ElasticSearchDruidDataSourceFactory.createDataSource(properties);
            druidDataSource.setInitialSize(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return druidDataSource;
    }


    @Bean
    public ElasticsearchUtil elasticsearchUtil() {
        ElasticsearchUtil elasticsearchUtil = new ElasticsearchUtil();
        elasticsearchUtil.setElasticFactory(getTransportClient());
        return elasticsearchUtil;
    }

    @Bean
    public ElastricSearchHelper elastricSearchHelper() {
        ElastricSearchHelper elastricSearchHelper = new ElastricSearchHelper();
        elastricSearchHelper.setElasticsearchUtil(elasticsearchUtil());
        return elastricSearchHelper;
    }

}
