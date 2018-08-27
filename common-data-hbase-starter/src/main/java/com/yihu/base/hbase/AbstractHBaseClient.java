package com.yihu.base.hbase;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

/**
 * AbstractHBaseClient - 基类
 * @author hzp
 * @created 2017.05.03
 */
public class AbstractHBaseClient {

    public AbstractHBaseClient(HbaseTemplate hbaseTemplate) {
        this.hbaseTemplate = hbaseTemplate;
    }

    protected HbaseTemplate hbaseTemplate;


    /**
     * 创建连接
     */
    protected Connection getConnection() throws Exception {
        return getConnection(hbaseTemplate);
    }


    /**
     * 创建连接
     */
    protected Connection getConnection(HbaseTemplate hbaseTemplate) throws Exception {
        Connection connection = ConnectionFactory.createConnection(hbaseTemplate.getConfiguration());
        return connection;
    }
}
