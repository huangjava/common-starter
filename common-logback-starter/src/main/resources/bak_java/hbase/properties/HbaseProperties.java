package com.yihu.base.hbase.properties;

/**
 * Created by chenweida on 2018/2/24.
 * ES相关配置
 */
public class HbaseProperties {

    public static final String default_zkZnodeParent = "/hbase-unsecure";//zk上的路径
    public static final String default_zkPort = "2181";//zk上的路径

    public  static final String rowkeyGenerate_uuid="UUID";

    private String tableName; //表明

    private String familyName;//列族名称 多个逗号分隔

    private String zkHosts;//zookeeper路劲

    private String zkPort;//zookeeper路劲
    /**
     * 对于Hortonworks：  hconfig.set("zookeeper.znode.parent", "/hbase-unsecure")

     对于cloudera：  hconfig.set("zookeeper.znode.parent", "/hbase")
     */
    private String zkZnodeParent ;//zk上的路径

    private String hdfsUserName;//hdfs用户名称

    private String rowkey;//rowkey规则  UUID


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String[] getFamilyNames() {
        return familyName.split(",");
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getZkHosts() {
        return zkHosts;
    }

    public void setZkHosts(String zkHosts) {
        this.zkHosts = zkHosts;
    }

    public String getZkZnodeParent() {
        return zkZnodeParent;
    }

    public void setZkZnodeParent(String zkZnodeParent) {
        this.zkZnodeParent = zkZnodeParent;
    }

    public String getHdfsUserName() {
        return hdfsUserName;
    }

    public void setHdfsUserName(String hdfsUserName) {
        this.hdfsUserName = hdfsUserName;
    }

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public void setZkPort(String zkPort) {
        this.zkPort = zkPort;
    }

    public String getZkPort() {
        return zkPort;
    }
}
