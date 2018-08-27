package com.yihu.admin.server.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

    private Logger logger= LoggerFactory.getLogger(HttpClientUtil.class);
    /**
     * 发送post请求
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param chatSet 编码格式
     * @return
     */
    public  String post(String url, List<NameValuePair> params, String chatSet) {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost(url);
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(params, chatSet);
            httppost.setEntity(uefEntity);
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity, chatSet);
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 发送get请求
     *
     * @param url     请求地址
     * @param chatSet 编码格式
     * @return
     */
    public  String get(String url, String chatSet) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            // 创建httpget.
            HttpGet httpget = new HttpGet(url);
            // 执行get请求.
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity, chatSet);
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            logger.error("http error:"+e.getMessage()+", url:"+url);
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * http调用方法，(健康之路开放平台)
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public  String httpPost(String url, Map<String, String> params) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            if (params != null && params.size() > 0) {
                List<NameValuePair> valuePairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    NameValuePair nameValuePair = new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue()));
                    valuePairs.add(nameValuePair);
                }
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(valuePairs, "UTF-8");
                httpPost.setEntity(formEntity);
            }
            CloseableHttpResponse resp = httpclient.execute(httpPost);
            try {
                HttpEntity entity = resp.getEntity();
                String respContent = EntityUtils.toString(entity, "UTF-8").trim();
                return respContent;
            } finally {
                resp.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            httpclient.close();
        }
    }

    /**
     * 获取加密后参数集合(健康之路开放平台)
     *
     * @param params
     * @return
     */
    public  Map<String, String> getSecretParams(Map<String, String> params, String appId, String secret) {
        String timestamp = Long.toString(System.currentTimeMillis());
        params.put("timestamp", timestamp);
        StringBuilder stringBuilder = new StringBuilder();

        // 对参数名进行字典排序  
        String[] keyArray = params.keySet().toArray(new String[0]);
        Arrays.sort(keyArray);
        // 拼接有序的参数名-值串  
        stringBuilder.append(appId);
        for (String key : keyArray) {
            stringBuilder.append(key).append(params.get(key));
        }
        String codes = stringBuilder.append(secret).toString();
        String sign = org.apache.commons.codec.digest.DigestUtils.shaHex(codes).toUpperCase();
        // 添加签名,并发送请求  
        params.put("appId", appId);
        params.put("sign", sign);

        return params;
    }



    /**
     * 发送post请求
     *
     * @param url     请求地址
     * @param params  请求参数
     * @return
     */
    public String iotPostBody(String url, String params) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        org.springframework.http.HttpEntity<String> formEntity = new org.springframework.http.HttpEntity<String>(params, headers);
        String ret = restTemplate.postForObject(url, formEntity, String.class);
        return ret;
    }
}