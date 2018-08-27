package com.yihu.admin.server.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * Created by chenweida on 2018/2/26.
 */
public class RollingUtil {
    /**
     * 在文件名最后追加日期  log_2017_01_01
     *
     * @param name
     * @param rolling
     * @return
     */
    public static String getRollingAppendLast(String name, String rolling) {
        if (StringUtils.isNoneEmpty(rolling)) {
            if ("day".equals(rolling)) {
                return new StringBuffer(name + "_" + LocalDate.now().toString()).toString();
            } else if ("week".equals(rolling)) {
                return new StringBuffer(name + "_" + LocalDate.now().minusWeeks(0).with(DayOfWeek.MONDAY)).toString();
            } else if ("month".equals(rolling)) {
                return new StringBuffer(name + "_" + LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())).toString();
            } else if ("year".equals(rolling)) {
                return new StringBuffer(name + "_" + LocalDate.now().with(TemporalAdjusters.firstDayOfYear())).toString();
            }
        }
        return name;
    }

    /**
     * 在文件名前面追加日期 2017_01_01_log
     *
     * @param name
     * @param rolling
     * @return
     */
    public static String getRollingAppendFirst(String name, String rolling) {

        return getRollingAppendFirst(name, rolling, "");
    }

    /**
     * 在文件名前面追加日期 2017_01_01_log
     *
     * @param name
     * @param rolling
     * @return
     */
    public static String getRollingAppendFirst(String name, String rolling, String suffix) {
        if (StringUtils.isNoneEmpty(suffix)) {
            suffix = File.pathSeparator + suffix; //多加一个 .
        }
        if (StringUtils.isNoneEmpty(rolling)) {
            if ("day".equals(rolling)) {
                return new StringBuffer(LocalDate.now().toString() + "_" + name + suffix).toString();
            } else if ("week".equals(rolling)) {
                return new StringBuffer(LocalDate.now().minusWeeks(0).with(DayOfWeek.MONDAY) + "_" + name + suffix).toString();
            } else if ("month".equals(rolling)) {
                return new StringBuffer(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()) + "_" + name + suffix).toString();
            } else if ("year".equals(rolling)) {
                return new StringBuffer(LocalDate.now().with(TemporalAdjusters.firstDayOfYear()) + "_" + name + suffix).toString();
            }
        }
        return name + suffix;
    }
}
