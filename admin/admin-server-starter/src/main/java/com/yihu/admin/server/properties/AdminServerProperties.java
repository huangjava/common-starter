package com.yihu.admin.server.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.yihu.admin.server.util.RollingUtil;

/**
 * Created by chenweida on 2018/5/15 0015.1
 */
@Component
public class AdminServerProperties {
    @Value("${yihu.admin.server.defaultIndexName:admin_log}")
    private String defaultIndexName;//默认是index
    @Value("${yihu.admin.server.defaultTypeName:admin_log}")
    private String defaultTypeName;//默认是type
    @Value("${yihu.admin.server.partition:month}")
    private String partition;//默认是 day week month year


    public String getDefaultIndexName() {
        return defaultIndexName;
    }

    public void setDefaultIndexName(String defaultIndexName) {
        this.defaultIndexName = defaultIndexName;
    }

    public String getDefaultTypeName() {
        return defaultTypeName;
    }

    public void setDefaultTypeName(String defaultTypeName) {
        this.defaultTypeName = defaultTypeName;
    }

    public String getPartition() {
        return partition;
    }

    public void setPartition(String partition) {
        this.partition = partition;
    }


    public String getRealDefaultIndexName() {
        return RollingUtil.getRollingAppendLast(getDefaultIndexName(), getPartition());
    }

}
