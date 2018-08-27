package bak.hdfs.properties;

import com.yihu.base.common.RollingUtil;

/**
 * Created by chenweida on 2018/2/26.
 */
public class HDFSProperties {

    private String hosts;//hdfs://192.168.131.240:9000/
    private String path;//日志的存储路径
    private String fileName;//日志的名称
    private String rolling;//日志按照什么滚动  day week month year
    private String suffix = "log";//后缀

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return RollingUtil.getRollingAppendFirst(fileName, rolling, suffix);
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getRolling() {
        return rolling;
    }

    public void setRolling(String rolling) {
        this.rolling = rolling;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
