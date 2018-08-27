package com.demo.router;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * Created by chenweida on 2018/5/31 0031.
 *
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class
}
)
public class RouterAppliaction {
    public static ApplicationContext ctx = null;

    public static void main(String[] args) {
        ctx = SpringApplication.run(RouterAppliaction.class, args);
    }


}