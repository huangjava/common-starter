package com.yihu.base.cache.support;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CacheInvocation {

    private Object key;

    private Object targetBean;

    private Method targetMethod;

    private Object[] arguments;

    private List<String> parameterTypes = new ArrayList<>();

    public CacheInvocation(Object key,Object targetBean,Method targetMethod,Object[] arguments,Class[] parameterTypes){
        this.key = key;
        this.targetBean = targetBean;
        this.targetMethod = targetMethod;
        if(null != arguments && arguments.length > 0){
            this.arguments = Arrays.copyOf(arguments,arguments.length);
        }
        if(null != parameterTypes && parameterTypes.length > 0){
            for( Class cls:parameterTypes){
                this.parameterTypes.add(cls.getName());
            }
        }
    }

    /**
     * 重写equals方法
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj){
        if(obj == this){
            return true;
        }
        if(null == obj || obj.getClass() != getClass()){
            return false;
        }
        CacheInvocation cacheInvocation = (CacheInvocation)obj;
        return key.equals(cacheInvocation.key);
    }

    /**
     * 重写hashCode方法
     * @return
     */
    @Override
    public int hashCode(){
        return key.hashCode();
    }

    public Object getKey() {
        return key;
    }

    public Object getTargetBean() {
        return targetBean;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
