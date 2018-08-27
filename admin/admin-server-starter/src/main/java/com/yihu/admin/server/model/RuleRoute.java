package com.yihu.admin.server.model;

/**
 * Created by chenweida on 2018/4/19 0019.1
 */
public class RuleRoute {
    /**
     * 主键
     */
    private String id;

    /**
     *  path是代理后的路径
     *  下面2种方式的path
     */
    private String path;

    /**
     * 微服务注册到发现服务的id
     * 即这种方式的 serviceId
     *  serviceId和url 2选1
     * zuul:
         routes:
           svr-simple1:    #这种方式和  svr-base: /base/**  一样  svr-base可以随便写 唯一即可
             path: /v1/simple2/**  #zuul path是代理后的路径
             serviceId: spring-cloud-svr-simple1  #serviceId是微服务name
     */
    private String serviceId;

    /**
     * 微服务的地址
     * 即下面这种方式的url
     *
     * zuul:
        routes:
          svr-simple2:     #这种方式和 上面一样 就是serviceId改成具体的url 但是这种配置方法不能利用eurika的负载均衡
            path: /v1/simple3/** #zuul path是代理后的路径
            url: http://localhost:10010/
     */
    private String url;


    /**
     * 失败是否重试
     * 在serviceId模式才有用
     */
    private Boolean retryable;
    /**
     * 是否有用
     */
    private Boolean enabled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public Boolean getRetryable() {
        return retryable;
    }

    public void setRetryable(Boolean retryable) {
        this.retryable = retryable;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "RuleRoute{" +
                "id='" + id + '\'' +
                ", path='" + path + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", url='" + url + '\'' +
                ", retryable=" + retryable +
                ", enabled=" + enabled +
                '}';
    }
}
