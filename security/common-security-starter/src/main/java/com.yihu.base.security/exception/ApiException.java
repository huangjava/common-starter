package com.yihu.base.security.exception;

/**
 * API 异常。使用错误代码初始化，并可接收用于补充错误消息的参数。
 * 用于描述错误代码的信息配置在各服务配置文件中，并由服务配置中心统一管理。
 * <p>
 * 错误描述结构，结构(字段errors对资源而言，REST规范错误不包含此结构)：
 * {
 * "message": "Validation Failed",
 * "document_url": "https://ehr.yihu.com/docs/api/somewhere"
 * "errors": [
 * {
 * "resource": "User",
 * "field": "title",
 * "code": "missing_field"
 * }
 * ]
 * }
 *
 * @author Sand
 * @version 1.0
 * @created 2015.12.20 16:05
 */
public class ApiException extends RuntimeException {
    private Integer errorCode = -10000;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public ApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ApiException() {
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }
}
