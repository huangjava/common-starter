package com.yihu.admin.client.advice;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.yihu.admin.client.properties.AdminClientProperties;
import com.yihu.admin.client.util.NetworkUtil;
import com.yihu.admin.client.websocket.buffer.EventBuffer;
import com.yihu.admin.client.websocket.event.HttpEvent;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * Created by chenweida on 2018/5/31 0031.
 */
@Component
@ConditionalOnProperty(name = "yihu.admin.client.advice.zuul.post", havingValue = "true")
public class ZuulPostFilter extends ZuulFilter {
    @Autowired
    private Tracer tracer;
    @Autowired
    private EventBuffer eventBuffer;
    @Autowired
    private AdminClientProperties adminClientProperties;
    @Value("${spring.application.name:unknow}")
    private String spanrName;

    @Override
    public String filterType() {
        //filterType：返回过滤器的类型。有pre、route、post、error等几种取值

        return "post";
    }

    @Override
    public int filterOrder() {
        //回一个int值来指定过滤器的执行顺序，不同的过滤器允许返回相同的数字
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //返回一个boolean值来判断该过滤器是否要执行，true表示执行，false表示不执行。
        return adminClientProperties.getZuulPost();
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();


        try {
            HttpEvent event = new HttpEvent();
            Long startTime = System.currentTimeMillis();
            String ip = NetworkUtil.getIpAddress(request);
            JSONObject headerObj = new JSONObject();
            Enumeration<String> headers = request.getHeaderNames();
            while (headers.hasMoreElements()) {
                String value = headers.nextElement();
                headerObj.put(value, request.getHeader(value));
            }

            event.setHeader(headerObj.toString());
            event.setEventStartTime(startTime);
            event.setEventEndTime(startTime);
            event.setExcuteTime(0L);
            event.setHeader(headerObj.toString());
            event.setEventName("system_zuul_tracer");
            event.setSpanName(spanrName);
            event.setSpanId(tracer.getCurrentSpan().getSpanId() + "");
            event.setTraceId(tracer.getCurrentSpan().traceIdString());
            event.setRequestParams(JSONObject.fromObject(request.getParameterMap()).toString());
            event.setSuccess(1);
            event.setUri(HttpAdvice.matchURI(request));
            event.setIp(ip);
            event.setMethod(request.getMethod());
            eventBuffer.addEvent(event);
        } catch (Exception e) {

        }

        HttpServletResponse response = ctx.getResponse();


        return response;
    }
}
