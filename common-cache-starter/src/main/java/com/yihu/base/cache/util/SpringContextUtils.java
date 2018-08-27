package com.yihu.base.cache.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring上下文管理器
 * @author LiTaohong
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext springContext = null;

    /**
     * 获取Spring应用上下文环境
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return springContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        springContext = applicationContext;
    }

    /**
     * 获取Bean
     *
     * @param serviceName
     * @param <T>
     * @return
     */
    public static <T> T getBean(String serviceName) {
        return (T) springContext.getBean(serviceName);
    }

    public static <T> T getBean(Class<T> beanCls) {
        return (T) springContext.getBean(beanCls);
    }

    /**
     * 获取服务，并用参数初始化对象。
     *
     * @param serviceName
     * @param args
     * @param <T>
     * @return
     */
    public static <T> T getBean(String serviceName, Object... args) {
        T ref = (T)springContext.getBean(serviceName, args);
        if (ref == null) return null;

        return ref;
    }

    public static <T> T getBean(Class<T> beanCls, Object... args){
        T ref = (T)springContext.getBean(beanCls, args);
        if (ref == null) return null;

        return ref;
    }

}
