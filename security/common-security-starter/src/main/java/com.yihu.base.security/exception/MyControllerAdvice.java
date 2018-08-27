package com.yihu.base.security.exception;

import com.yihu.base.security.vo.BaseEnvelop;
import com.yihu.base.security.vo.BaseEnvelopStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;

/**
 * 统一异常处理器
 * Created by 刘文彬 on 2018/5/9.
 */
@ControllerAdvice
public class MyControllerAdvice {

    @Autowired
    private Tracer tracer;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @ResponseBody
    @ExceptionHandler(value = ApiException.class)
    public BaseEnvelop myErrorHandler(ApiException ex) {
        tracer.getCurrentSpan().logEvent(ex.getMessage());
        logger.error(ex.getMessage());
        return BaseEnvelop.getError(ex.getMessage(), ex.getErrorCode());
    }

    @ResponseBody
    @ExceptionHandler(value = ParseException.class)
    public BaseEnvelop myErrorHandler(ParseException ex) {
        tracer.getCurrentSpan().logEvent(BaseEnvelopStatus.status_10200.getName()+ex.getMessage());
        logger.error(BaseEnvelopStatus.status_10200.getName()+ex.getMessage());
        return BaseEnvelop.getError(BaseEnvelopStatus.status_10200.getName()+ex.getMessage(), BaseEnvelopStatus.status_10200.getCode());
    }
//
//    @ResponseBody
//    @ExceptionHandler(value = UsernameNotFoundException.class)
//    public BaseEnvelop myErrorHandler(UsernameNotFoundException ex) {
//        tracer.getCurrentSpan().logEvent(BaseEnvelopStatus.status_10200.getName()+ex.getMessage());
//        logger.error(BaseEnvelopStatus.status_10200.getName()+ex.getMessage());
//        return BaseEnvelop.getError(BaseEnvelopStatus.status_10200.getName()+ex.getMessage(), BaseEnvelopStatus.status_10200.getCode());
//    }

}
