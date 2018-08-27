package com.yihu.base.security.vo;

/**
 * Created by 刘文彬 on 2018/5/4.
 */
public class BaseSecurityRequestMapping {
    public static final String api_common="/security";

    public static class BaseToken {
        public static final String api_update_token_expiration_time = api_common+"/update/tokenExpiration/time";
        public static final String api_update_token_expiration_second = api_common+"/update/tokenExpiration/second";
        public static final String api_update_token_expiration_second2 = api_common+"/update/tokenExpiration/second2";
        public static final String api_update_token_expiration = api_common+"/update/tokenExpiration";
    }
}
