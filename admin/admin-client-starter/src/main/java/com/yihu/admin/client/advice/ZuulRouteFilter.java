package com.yihu.admin.client.advice;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.yihu.admin.client.properties.AdminClientProperties;
import com.yihu.admin.client.util.NetworkUtil;
import com.yihu.admin.client.websocket.buffer.EventBuffer;
import com.yihu.admin.client.websocket.event.HttpEvent;
import net.sf.json.JSONObject;
import okhttp3.*;
import okhttp3.internal.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * Created by chenweida on 2018/5/31 0031.
 * 目前还有bug 要解决跳转之后的
 */
@Component
@ConditionalOnProperty(name = "yihu.admin.client.advice.zuul.route", havingValue = "true")
public class ZuulRouteFilter extends ZuulFilter {
    private Logger logger = LoggerFactory.getLogger(ZuulRouteFilter.class);
    @Autowired
    private Tracer tracer;
    @Autowired
    private EventBuffer eventBuffer;
    @Autowired
    private AdminClientProperties adminClientProperties;
    @Value("${spring.application.name:unknow}")
    private String spanrName;
    @Autowired
    private ProxyRequestHelper helper;
    @Autowired
    private okhttp3.OkHttpClient httpClient;

    @Override
    public String filterType() {
        //filterType：返回过滤器的类型。有pre、route、post、error等几种取值

        return "route";
    }

    @Override
    public int filterOrder() {
        //回一个int值来指定过滤器的执行顺序，不同的过滤器允许返回相同的数字
        return 600;
    }

    @Override
    public boolean shouldFilter() {
        //返回一个boolean值来判断该过滤器是否要执行，true表示执行，false表示不执行。
        return adminClientProperties.getZuulRoute();
    }

    @Override
    public Object run() {
        try {
            RequestContext ctx = RequestContext.getCurrentContext();
            HttpServletRequest request = ctx.getRequest();


            String method = request.getMethod();

            String uri = this.helper.buildZuulRequestURI(request);

            Headers.Builder headers = new Headers.Builder();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                Enumeration<String> values = request.getHeaders(name);

                while (values.hasMoreElements()) {
                    String value = values.nextElement();
                    headers.add(name, value);
                }
            }
            String address="";
            URL hostId = RequestContext.getCurrentContext().getRouteHost();
            Object serviceId = RequestContext.getCurrentContext().get("serviceId");
            if(hostId!=null){
                address=hostId.toString();
            }else if(!StringUtils.isEmpty(serviceId)){
                address="http://"+serviceId.toString();
            }
            InputStream inputStream = request.getInputStream();

            RequestBody requestBody = null;
            if (inputStream != null && HttpMethod.permitsRequestBody(method)) {
                MediaType mediaType = null;
                if (headers.get("Content-Type") != null) {
                    mediaType = MediaType.parse(headers.get("Content-Type"));
                }
                requestBody = RequestBody.create(mediaType, StreamUtils.copyToByteArray(inputStream));
            }

            Request.Builder builder = new Request.Builder()
                    .headers(headers.build())
                    .url(address+uri)
                    .method(method, requestBody);
            //日志
            HttpEvent event = new HttpEvent();
            Long startTime = System.currentTimeMillis();
            String ip = NetworkUtil.getIpAddress(request);
            JSONObject headerObj = new JSONObject();
            Enumeration<String> headersed = request.getHeaderNames();
            while (headersed.hasMoreElements()) {
                String value = headersed.nextElement();
                headerObj.put(value, request.getHeader(value));
            }

            Response response = httpClient.newCall(builder.build()).execute();
            try {
                Long endTime = System.currentTimeMillis();
                event.setHeader(headerObj.toString());
                event.setEventStartTime(startTime);
                event.setEventEndTime(endTime);
                event.setExcuteTime(endTime - startTime);
                event.setHeader(headerObj.toString());
                event.setEventName("system_zuul_tracer");
                event.setResponseParams(response.body().string());
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
                e.printStackTrace();
                logger.error(e.getMessage());
            }

            LinkedMultiValueMap<String, String> responseHeaders = new LinkedMultiValueMap<>();

            for (Map.Entry<String, List<String>> entry : response.headers().toMultimap().entrySet()) {
                responseHeaders.put(entry.getKey(), entry.getValue());
            }

            this.helper.setResponse(response.code(), response.body().byteStream(), responseHeaders);
            ctx.setRouteHost(null); // prevent SimpleHostRoutingFilter from running


        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }


        return null;
    }
}
