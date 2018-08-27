package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * Created by chenweida on 2018/2/24
 * http://localhost:8082/admin/index/index.html
 */
@SpringBootApplication
public class DemoEurekaApplication {
    public static ApplicationContext ctx = null;

    public static void main(String[] args) {
        ctx = SpringApplication.run(DemoEurekaApplication.class, args);
    }


}