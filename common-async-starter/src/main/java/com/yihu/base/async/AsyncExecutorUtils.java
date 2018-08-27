package com.yihu.base.async;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步线程类
 */
@Configuration
@EnableAsync
public class AsyncExecutorUtils implements AsyncConfigurer {
    private ThreadPoolTaskExecutor taskExecutor = null;

    @Value("core-size")
    private int coreSize;

    @Value("max-size")
    private int maxSize;

    @Value("quenue-capacity")
    private int queueCapacity;

    @Value("keep-alive-seconds")
    private int aliveSeconds;

    @Override
    public Executor getAsyncExecutor() {

        taskExecutor.setCorePoolSize(5);

        taskExecutor.setMaxPoolSize(20);

        taskExecutor.setQueueCapacity(25);

        taskExecutor.setKeepAliveSeconds(120);

        // 线程池对拒绝任务(无线程可用)的处理策略 ThreadPoolExecutor.CallerRunsPolicy策略 ,调用者的线程会执行该任务,如果执行器已关闭,则丢弃.
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());

        taskExecutor.initialize();

        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }

}
