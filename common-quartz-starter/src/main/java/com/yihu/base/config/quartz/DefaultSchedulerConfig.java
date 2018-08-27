package com.yihu.base.config.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by chenweida on 2016/2/3.
 */
@Configuration
public class DefaultSchedulerConfig {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private DefaultJobFactory jobFactory;
    @Autowired
    private DataSource dataSource;

    @Value("${quartz.namespace}")
    private String namspace = "default_namespace";
    @Value("${quartz.overwriteExistingJobs}")
    private Boolean OverwriteExistingJobs = true;

    @Bean("SchedulerFactoryBean")
    SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        bean.setJobFactory(jobFactory);
        bean.setApplicationContext(this.applicationContext);
        bean.setOverwriteExistingJobs(OverwriteExistingJobs);
        bean.setStartupDelay(20);// 延时启动
        bean.setAutoStartup(true);
        bean.setDataSource(dataSource);
        bean.setBeanName(namspace);
        bean.setQuartzProperties(quartzProperties());
        return bean;
    }

    /**
     * quartz配置文件
     *
     * @return
     * @throws IOException
     */
    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }
}

