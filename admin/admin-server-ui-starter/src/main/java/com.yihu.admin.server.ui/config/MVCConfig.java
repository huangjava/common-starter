package com.yihu.admin.server.ui.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by chenweida on 2018/5/15 0015.
 */
@Configuration
public class MVCConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/demo/**").addResourceLocations("classpath:/demo/");
        registry.addResourceHandler("/admin/**").addResourceLocations("classpath:/admin/");
        super.addResourceHandlers(registry);

    }
}
