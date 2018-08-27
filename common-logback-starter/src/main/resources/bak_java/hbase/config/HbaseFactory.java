package com.yihu.base.hbase.config;

import com.yihu.base.hbase.properties.HbaseProperties;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chenweida on 2018/2/27.
 */
public class HbaseFactory {

    private HbaseTemplate hbaseTemplate = new HbaseTemplate();

    private HbaseProperties hbaseProperties;

    private volatile Configuration configuration;

    public HbaseFactory(HbaseProperties hbaseProperties) {
        this.hbaseProperties = hbaseProperties;
    }


    /**
     * 批量新增行
     */
    public void addLogBulk(String tableName, List<String> rowkeyList, List<Map<String, Map<String, String>>> familyList) throws Exception {
        hbaseTemplate.execute(tableName, new TableCallback<String>() {
            @Override
            public String doInTable(HTableInterface table) throws Throwable {
                List<Put> list = new ArrayList<>();
                for (int i = 0; i < rowkeyList.size(); i++) {
                    Put p = new Put(rowkeyList.get(i).getBytes());
                    Map<String, Map<String, String>> family = familyList.get(i);
                    for (String familyName : family.keySet()) {
                        Map<String, String> map = family.get(familyName);
                        for (String qualifier : map.keySet()) {
                            String value = map.get(qualifier);
                            if (value == null) {
                                continue;
                            }
                            p.add(familyName.getBytes(), qualifier.getBytes(), value.getBytes());
                        }
                    }
                    list.add(p);
                }
                table.put(list);
                return null;
            }
        });
    }

    public void init() {
        Connection connection = null;
        HBaseAdmin hBaseAdmin = null;
        try {
            //获取链接
            connection = getConnection();
            hBaseAdmin = (HBaseAdmin) connection.getAdmin();

            //判断表名是否存在
            if (!hBaseAdmin.tableExists(hbaseProperties.getTableName())) {
                //创建表
                createTable(hbaseProperties);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (hBaseAdmin != null) {
                    hBaseAdmin.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取链接
     *
     * @return
     * @throws IOException
     */
    private Connection getConnection() throws IOException {
        if (configuration == null) {
            synchronized (HbaseFactory.class) {
                if (configuration == null) {
                    //设置hadoop账号
                    System.setProperty("HADOOP_USER_NAME", hbaseProperties.getHdfsUserName());

                    configuration = HBaseConfiguration.create();

                    configuration.set("hbase.zookeeper.quorum", hbaseProperties.getZkHosts());
                    if(StringUtils.isEmpty(hbaseProperties.getZkZnodeParent())){
                        hbaseProperties.setZkZnodeParent(HbaseProperties.default_zkZnodeParent);
                    }
                    configuration.set("zookeeper.znode.parent",hbaseProperties.getZkZnodeParent());
                    if(StringUtils.isEmpty(hbaseProperties.getZkPort())){
                        hbaseProperties.setZkPort(HbaseProperties.default_zkPort);
                    }
                    configuration.set("hbase.zookeeper.property.clientPort",hbaseProperties.getZkPort());
                    hbaseTemplate.setConfiguration(configuration);
                }
            }
        }
        return ConnectionFactory.createConnection(configuration);
    }

    /**
     * 创建表
     *
     * @param hbaseProperties
     * @throws Exception
     */
    private void createTable(HbaseProperties hbaseProperties) throws Exception {
        Connection connection = getConnection();
        HBaseAdmin hBaseAdmin = (HBaseAdmin) connection.getAdmin();
        HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(hbaseProperties.getTableName()));

        //最多建议1-3个列族
        for (String family : hbaseProperties.getFamilyNames()) {
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(family);

            hColumnDescriptor.setBlockCacheEnabled(true);//开始读内存缓存
            hColumnDescriptor.setInMemory(true);//是否加载到内存
            hColumnDescriptor.setMaxVersions(1);//版本数1


            hTableDescriptor.addFamily(hColumnDescriptor);
        }

        hBaseAdmin.createTable(hTableDescriptor);

        hBaseAdmin.close();
        connection.close();
    }


}
