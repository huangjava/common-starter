package com.yihu.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * Created by chenweida on 2018/2/24.
 */

@SpringBootApplication
public class DemoSocketClientApplication {
    public static ApplicationContext ctx = null;

    public static void main(String[] args) {
        ctx = SpringApplication.run(DemoSocketClientApplication.class, args);
    }


}