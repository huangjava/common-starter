package com.yihu.base.hbase.rowkey.impl;

import com.yihu.base.hbase.rowkey.IRowkeyGenerate;

import java.util.UUID;

/**
 * Created by chenweida on 2018/2/28.
 */
public class UUIDRowkeyGenerate implements IRowkeyGenerate {
    @Override
    public String getRowkey() {
        return UUID.randomUUID().toString().replace("-","");
    }
}
