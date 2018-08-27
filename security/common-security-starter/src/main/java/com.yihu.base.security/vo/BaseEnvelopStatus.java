package com.yihu.base.security.vo;

/**
 * Created by chenweida on 2018/5/8 0008.
 */
public enum BaseEnvelopStatus {
    success("200请求成功", 200),
    system_error("-10000系统错误",-10000),
    //------------------登陆 权限相关 start ------------------
    status_10100("10100账号不存在", 10100),
    status_10101("10101密码错误", 10101),
    status_10102("10102用户未登录", 10102),
    status_10103("10103登陆超时", 10103),
    status_10104("10104账号被挤", 10104),
    status_10105("10105账号没权限", 10105),
    status_10106("10106账号已存在", 10106),
    //------------------登陆 权限相关 end ------------------
    status_10200("格式转换错误",10200);

    BaseEnvelopStatus(String name, Integer code) {
        this.name = name;
        this.code = code;
    }

    private String name;
    private Integer code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
