package com.yihu.admin.server.log;

/**
 * Created by chenweida on 2018/5/24 0024.1
 */
public class QuotaModel {

    private String uri;//接口路径
    private Integer allcount; //请求总次数
    private Integer success;  //成功次数
    private Integer fail;  // 失败次数
    private Double avgtime;  //请求平均时间
    private Integer maxtime;   // 最长请求时间

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getAllcount() {
        return allcount;
    }

    public void setAllcount(Integer allcount) {
        this.allcount = allcount;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getFail() {
        return fail;
    }

    public void setFail(Integer fail) {
        this.fail = fail;
    }

    public Double getAvgtime() {
        return avgtime;
    }

    public void setAvgtime(Double avgtime) {
        this.avgtime = avgtime;
    }

    public Integer getMaxtime() {
        return maxtime;
    }

    public void setMaxtime(Integer maxtime) {
        this.maxtime = maxtime;
    }
}
