package com.yihu.base.hbase.rowkey;

import com.yihu.base.hbase.properties.HbaseProperties;
import com.yihu.base.hbase.rowkey.impl.UUIDRowkeyGenerate;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by chenweida on 2018/2/28.
 */
public class RowkeyFactory {

    private volatile static IRowkeyGenerate rowkeyGenerate = null;

    private RowkeyFactory() {

    }

    public static String getRowkey(HbaseProperties hbaseProperties) {
        //初始化rowkey生成器
        if (rowkeyGenerate == null) {
            synchronized (RowkeyFactory.class) {
                if (rowkeyGenerate == null) {
                    initIRowkeyGenerate(hbaseProperties);
                }
            }
        }
        return rowkeyGenerate.getRowkey();
    }


    private static void initIRowkeyGenerate(HbaseProperties hbaseProperties) {
        //如果为空默认uuid
        if (StringUtils.isNoneEmpty(hbaseProperties.getRowkey())) {
            rowkeyGenerate = new UUIDRowkeyGenerate();
        } else {
            switch (hbaseProperties.getRowkey()) {
                case HbaseProperties.rowkeyGenerate_uuid: {
                    rowkeyGenerate = new UUIDRowkeyGenerate();
                    return;
                }
                default: {
                    rowkeyGenerate = new UUIDRowkeyGenerate();
                    return;
                }
            }
        }
    }
}
