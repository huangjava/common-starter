package com.yihu.base.hbase.properties;

/**
 * Created by chenweida on 2018/2/24.
 */
public class HbaseAppenderProperties {

    private HbaseProperties hbaseProperties=new HbaseProperties();
   //缓存区相关的配置
    private BufferProperties bufferProperties = new BufferProperties();

    public HbaseProperties getHbaseProperties() {
        return hbaseProperties;
    }

    public void setHbaseProperties(HbaseProperties hbaseProperties) {
        this.hbaseProperties = hbaseProperties;
    }

    public BufferProperties getBufferProperties() {
        return bufferProperties;
    }

    public void setBufferProperties(BufferProperties bufferProperties) {
        this.bufferProperties = bufferProperties;
    }

}
