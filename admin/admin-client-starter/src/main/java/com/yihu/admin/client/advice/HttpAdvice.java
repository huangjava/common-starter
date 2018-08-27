package com.yihu.admin.client.advice;

import com.yihu.admin.client.properties.AdminClientProperties;
import com.yihu.admin.client.util.NetworkUtil;
import com.yihu.admin.client.util.SpringUtil;
import com.yihu.admin.client.websocket.buffer.EventBuffer;
import com.yihu.admin.client.websocket.event.HttpEvent;
import net.sf.json.JSONObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by chenweida on 2018/5/23 0023.1
 */
@Aspect
@Component
@ConditionalOnProperty(name = "yihu.admin.client.advice.http", havingValue = "true")
public class HttpAdvice {
    @Autowired
    private Tracer tracer;
    @Autowired
    private EventBuffer eventBuffer;
    @Autowired
    private AdminClientProperties adminClientProperties;
    @Value("${spring.application.name:unknow}")
    private String spanrName;

    private Logger logger = LoggerFactory.getLogger(HttpAdvice.class);


    private static List<RequestToMethodItem> getURL = new ArrayList<>();
    private static List<RequestToMethodItem> postURL = new ArrayList<>();
    private static List<RequestToMethodItem> putURL = new ArrayList<>();
    private static List<RequestToMethodItem> deleteURL = new ArrayList<>();
    @Autowired
    private SpringUtil springUtill;

    private static AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 初始化路径
     */
    @PostConstruct
    public void initAllUrl() {
        RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping) springUtill.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> requestMappingInfoHandlerMethodEntry : handlerMethods.entrySet()) {
            try {
                RequestMappingInfo requestMappingInfo = requestMappingInfoHandlerMethodEntry.getKey();
                HandlerMethod mappingInfoValue = requestMappingInfoHandlerMethodEntry.getValue();

                PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();
                List<String> requestUrlList = new ArrayList<>(patternsCondition.getPatterns());
                String requestUrl = requestUrlList.get(0);
                logger.info("requestUrl:" + requestUrl);

                RequestMethodsRequestCondition methodCondition = requestMappingInfo.getMethodsCondition();
                List<RequestMethod> requestTypeList = new ArrayList<>(methodCondition.getMethods());
                String requestType = requestTypeList.get(0).name();
                logger.info("requestType:" + requestType);


                String controllerName = mappingInfoValue.getBeanType().toString();
                String requestMethodName = mappingInfoValue.getMethod().getName();
                Class<?>[] methodParamTypes = mappingInfoValue.getMethod().getParameterTypes();
                RequestToMethodItem item = new RequestToMethodItem(requestUrl,requestType , controllerName, requestMethodName, methodParamTypes);
                if ("get".equals(requestType.toLowerCase())) {
                    getURL.add(item);
                }
                if ("post".equals(requestType.toLowerCase())) {
                    postURL.add(item);
                }
                if ("put".equals(requestType.toLowerCase())) {
                    putURL.add(item);
                }
                if ("delete".equals(requestType.toLowerCase())) {
                    deleteURL.add(item);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    //    @Around("within(@org.springframework.stereotype.Controller *) " +
//            "within(@org.springframework.web.bind.annotation.RestController *)")
    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object process(ProceedingJoinPoint point) throws Throwable {
        if (!adminClientProperties.getHttp()) {
            return point.proceed();
        }
        HttpEvent event = new HttpEvent();
        event.setSpanName(spanrName);
        HttpServletRequest request = null;
        if (RequestContextHolder.getRequestAttributes() != null) {
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String ip = NetworkUtil.getIpAddress(request);
            event.setUri(matchURI(request));
            event.setIp(ip);
            event.setMethod(request.getMethod());
            try {
                JSONObject headerObj = new JSONObject();
                Enumeration<String> headers = request.getHeaderNames();
                while (headers.hasMoreElements()) {
                    String value = headers.nextElement();
                    headerObj.put(value, request.getHeader(value));
                }
                event.setHeader(headerObj.toString());
            } catch (Exception e) {

            }
        }
        //访问目标方法的参数：
        Object[] args = point.getArgs();

        event.setRequestParams(JSONObject.fromObject(request.getParameterMap()).toString());
        Long strartTime = System.currentTimeMillis();
        event.setEventStartTime(strartTime);
        try {
            Object returnValue = point.proceed(args);
            Long endTime = System.currentTimeMillis();
            if (returnValue != null) {
                try {
                    event.setResponseParams(JSONObject.fromObject(returnValue).toString());
                } catch (Exception e) {
                    event.setResponseParams(returnValue.toString());
                }
            }
            event.setExcuteTime(endTime - strartTime);
            event.setEventName("system_http_tracer");
            event.setEventEndTime(endTime);
            event.setSpanId(tracer.getCurrentSpan().getSpanId() + "");
            event.setTraceId(tracer.getCurrentSpan().traceIdString());
            event.setSuccess(1);
            eventBuffer.addEvent(event);
            logger.debug("event:" + event.toString());
            return returnValue;
        } catch (Exception e) {
            Long endTime = System.currentTimeMillis();
            event.setExcuteTime(endTime - strartTime);
            event.setEventName("system_http_tracer");
            event.setEventEndTime(endTime);
            event.setSpanId(tracer.getCurrentSpan().getSpanId() + "");
            event.setTraceId(tracer.getCurrentSpan().traceIdString());
            event.setFail(1);
            eventBuffer.addEvent(event);
            logger.debug("event:" + event.toString());

            throw new Exception(e);
        }

    }

    public static String matchURI(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        List<RequestToMethodItem> urls = null;
        if ("get".equals(method.toLowerCase())) {
            urls = getURL;
        } else if ("post".equals(method.toLowerCase())) {
            urls = postURL;
        } else if ("put".equals(method.toLowerCase())) {
            urls = putURL;
        } else if ("delete".equals(method.toLowerCase())) {
            urls = deleteURL;
        }
        for (int i = 0; i < urls.size(); i++) {
            String one = urls.get(i).getRequestUrl();
            if (antPathMatcher.match(one, requestURI)) {
                return one;
            }
        }
        return requestURI;
    }
}
