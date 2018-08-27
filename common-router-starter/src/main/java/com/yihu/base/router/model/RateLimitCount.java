package com.yihu.base.router.model;

/**
 * Created by chenweida on 2018/4/27 0027.
 */
public class RateLimitCount {
    private Integer count;//访问次数
    private String startTime;//开始时间  yyyy-MM-dd HH:mm:ss
    private String endTime;//结束时间 yyyy-MM-dd HH:mm:ss

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
