package com.yihu.base.router.model;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by chenweida on 2018/4/27 0027.
 */
public class Result {
    @ApiModelProperty("成功信息")
    protected String message;
    @ApiModelProperty("状态（200成功，-1是失败）")
    protected Integer status = 200;

    public Result() {
    }

    public Result(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public boolean getSuccess() {
        return 200 == status ? true : false;
    }
}
