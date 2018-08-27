package com.yihu.base;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author litaohong on 2018/5/7
 * @project jkzl-start
 */
@Configuration
public class SolrConfig {

    @Bean
    public SolrHelper solrHelper() {
        SolrHelper solrHelper = new SolrHelper();
        return solrHelper;
    }

    @Bean
    public SolrPool solrPool() {
        SolrPool solrPool = new SolrPool();
        return solrPool;
    }

}
