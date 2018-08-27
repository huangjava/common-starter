package com.yihu.base.cache.support;

import com.yihu.base.cache.support.CacheSupport;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;

@Aspect
@Component
public class CachingAnnoationAspect {

    @Autowired
    private CacheSupport cacheSupport;

    private <T extends Annotation> List<T> getMethodAnnotations(AnnotatedElement annotatedElement,Class<T> annotationType){
        List<T> anns = new ArrayList<T>();
        T ann = annotatedElement.getAnnotation(annotationType);
        if (null != ann) {
            anns.add(ann);
        }
        for(Annotation annotation:annotatedElement.getAnnotations()){
            ann = annotation.annotationType().getAnnotation(annotationType);
            if(null != ann){
                anns.add(ann);
            }
        }
        return anns.isEmpty() ? null : anns;
    }

    private Method getSpecificMethod(ProceedingJoinPoint proceedingJoinPoint){
        MethodSignature methodSignature = (MethodSignature)proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(proceedingJoinPoint.getTarget());
        if(null == targetClass && null != proceedingJoinPoint.getTarget()){
            targetClass = proceedingJoinPoint.getTarget().getClass();
        }
        Method specificMethod = ClassUtils.getMostSpecificMethod(method,targetClass);
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        return specificMethod;
    }

    @Pointcut("@annotation(org.springframework.cache.annotation.Cacheable)")
    public void pointcut(){}


    @Around("pointcut()")
    public Object registerInvocation(ProceedingJoinPoint proceedingJoinPoint){
        Object object = null;
        Method method = getSpecificMethod(proceedingJoinPoint);
        List<Cacheable> annotations =  getMethodAnnotations(method,Cacheable.class);
        if(null == annotations){
            return object;
        }

        Set<String> cacheSet = new HashSet<>();
        String cacheKey = "";
        for (Cacheable cacheables:annotations) {
             cacheSet.addAll(Arrays.asList(cacheables.value()));
             cacheKey = cacheables.key();
        }
        if(proceedingJoinPoint.getSignature() instanceof MethodSignature){
            Class[] parameterTpyes = ((MethodSignature) proceedingJoinPoint.getSignature()).getParameterTypes();
            cacheSupport.registerInvocation(proceedingJoinPoint.getTarget(),method,parameterTpyes,proceedingJoinPoint.getArgs(),cacheSet,cacheKey);
        }
        try {
            object = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return object;
    }

}
