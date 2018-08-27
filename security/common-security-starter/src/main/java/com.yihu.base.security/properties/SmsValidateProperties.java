package com.yihu.base.security.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by chenweida on 2017/12/5.
 */
@Component
public class SmsValidateProperties {

    @Value("${security.oauth2.sms.expireIn:1}")
    private Integer expireIn = 1; //短信验证码过期时间

    @Value("${security.oauth2.sms.length:6}")
    private Integer length = 6; //短信验证码长度

    public Integer getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(Integer expireIn) {
        this.expireIn = expireIn;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}
