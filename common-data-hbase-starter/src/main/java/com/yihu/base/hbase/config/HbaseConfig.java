package com.yihu.base.hbase.config;

import com.yihu.base.hbase.HBaseHelper;
import com.yihu.base.hbase.HBaseAdmin;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Sand
 * @version 1.0
 * @created 2015.11.28 16:26
 */
@Configuration
@ConfigurationProperties(prefix = "hadoop")
public class HbaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(HbaseConfig.class);

    private Map<String, String> hbaseProperties = new HashMap<>();

    public Map<String, String> getHbaseProperties() {
        return this.hbaseProperties;
    }

    @Value("${hadoop.user.name}")
    private String user;

    @Bean
    public org.apache.hadoop.conf.Configuration configuration() {
        Set<String> keys = new HashSet<>(hbaseProperties.keySet());
        for (String key : keys) {
            String value = hbaseProperties.remove(key);
            key = key.replaceAll("^\\d{1,2}\\.", "");

            hbaseProperties.put(key, value);
        }
        org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();
        hbaseProperties.keySet().stream().filter(key -> hbaseProperties.get(key) != null).forEach(key -> {
            configuration.set(key, hbaseProperties.get(key));
        });
        return configuration;
    }

    @Bean
    public HbaseTemplate hbaseTemplate(org.apache.hadoop.conf.Configuration configuration) {
        logger.info("set System property for hbase ---", user);
        System.setProperty("HADOOP_USER_NAME", user);
        HbaseTemplate hbaseTemplate = new HbaseTemplate();
        hbaseTemplate.setConfiguration(configuration);
        /*try {
            String tableName = "HealthProfile";
            //覆盖默认的配置文件
            org.apache.hadoop.conf.Configuration.addDefaultResource("core-site.xml");
            org.apache.hadoop.conf.Configuration.addDefaultResource("hbase-site.xml");
            Connection connection = ConnectionFactory.createConnection(configuration);
            logger.info("Hbase createConnection finished---", connection.getConfiguration());
            Admin admin = connection.getAdmin();
            boolean ex = admin.tableExists(TableName.valueOf(tableName));
            //判断是否存在
            if (ex) {
                hbaseTemplate.execute(tableName, new TableCallback<Object>() {
                    @Override
                    public Object doInTable(HTableInterface table) throws Throwable {
                        Get get = new Get(Bytes.toBytes("connection-init"));
                        Result result = table.get(get);

                        return result;
                    }
                });
            }
            admin.close();
            connection.close();
        } catch (Exception ex) {
            logger.info("Hbase createConnection failure", ex.getMessage());
        }*/
        return hbaseTemplate;
    }

    @Bean
    public HBaseHelper hBaseHelper(HbaseTemplate hbaseTemplate) {
        HBaseHelper hBaseHelper = new HBaseHelper(hbaseTemplate);
        return hBaseHelper;
    }

    @Bean
    public HBaseAdmin hBaseAdmin(HbaseTemplate hbaseTemplate) {
        HBaseAdmin hBaseAdmin = new HBaseAdmin(hbaseTemplate);
        return hBaseAdmin;
    }

}
