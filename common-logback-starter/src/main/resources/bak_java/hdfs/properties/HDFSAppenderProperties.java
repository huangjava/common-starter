package bak.hdfs.properties;

/**
 * Created by chenweida on 2018/2/26.
 */
public class HDFSAppenderProperties {

    private HDFSProperties hdfsProperties=new HDFSProperties();

    private BufferProperties bufferProperties=new BufferProperties();

    public HDFSProperties getHdfsProperties() {
        return hdfsProperties;
    }

    public void setHdfsProperties(HDFSProperties hdfsProperties) {
        this.hdfsProperties = hdfsProperties;
    }

    public BufferProperties getBufferProperties() {
        return bufferProperties;
    }

    public void setBufferProperties(BufferProperties bufferProperties) {
        this.bufferProperties = bufferProperties;
    }
}
