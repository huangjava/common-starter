package com.yihu.base.security.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by chenweida on 2018/1/16.
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class BaseEnvelop {

    protected String message;
    protected Integer status = 200;

    public BaseEnvelop() {
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static BaseEnvelop getSuccess(String message) {
        BaseEnvelop envelop = new BaseEnvelop();
        envelop.setMessage(message);
        envelop.setStatus(200);
        return envelop;
    }

    public static BaseEnvelop getError(String message, int errorCode) {
        BaseEnvelop envelop = new BaseEnvelop();
        envelop.setMessage(message);
        envelop.setStatus(errorCode);
        return envelop;
    }

    public static BaseEnvelop getError(String message) {
        BaseEnvelop envelop = new BaseEnvelop();
        envelop.setMessage(message);
        envelop.setStatus(-1);
        return envelop;
    }

}
