package com.yihu.base.security.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by chenweida on 2017/12/5.
 */
@Component
public class AccessTokenPorperties {
    @Value("${security.oauth2.token.accessTokenValidityHours:2}")
    private Integer accessTokenValidityHours ; //accesstoken超时时间

    @Value("${security.oauth2.token.refreshTokenValidityHours:2}")
    private Integer refreshTokenValidityHours ;//刷新token过期时间

    @Value("${security.oauth2.token.tokenType:accessToken}")
    private String tokenType;



    public Integer getAccessTokenValidityHours() {
        return accessTokenValidityHours;
    }

    public void setAccessTokenValidityHours(Integer accessTokenValidityHours) {
        this.accessTokenValidityHours = accessTokenValidityHours;
    }

    public Integer getRefreshTokenValidityHours() {
        return refreshTokenValidityHours;
    }

    public void setRefreshTokenValidityHours(Integer refreshTokenValidityHours) {
        this.refreshTokenValidityHours = refreshTokenValidityHours;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }



}
