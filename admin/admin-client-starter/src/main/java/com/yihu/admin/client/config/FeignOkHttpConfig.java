package com.yihu.admin.client.config;

import feign.Feign;
import okhttp3.ConnectionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.feign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Created by chenweida on 2018/6/4 0004.
 */
@Configuration
@ConditionalOnProperty(name = "yihu.admin.client.advice.zuul.route", havingValue = "true")
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class FeignOkHttpConfig {


    @Bean
    public okhttp3.OkHttpClient okHttpClient(){
        return new okhttp3.OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool())
                // .addInterceptor();
                .build();
    }
}