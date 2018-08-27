package com.yihu.base.hbase.timer;

import java.util.Timer;

/**
 * Created by chenweida on 2018/2/27.
 * 优化hbase效率的时候可以手动关闭major
 * hbase.hregion.majorcompaction
 * 然后自己定时在凌晨的时候去合并，因为在合并的时候无法操作文件
 */
public class MajorTimer extends Timer {
}
