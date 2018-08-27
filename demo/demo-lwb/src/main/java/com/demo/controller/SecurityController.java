package com.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Administrator on 2018/4/3 0003.
 */
@RestController
@RequestMapping("/login")
@Api(tags = "登陆相关", description = "登陆相关demo")
public class SecurityController {

    private RestTemplate restTemplate = new RestTemplate();

    /**
     * @return
     */
    @RequestMapping(value = "/login_usernamePassword", method = RequestMethod.POST)
    @ApiOperation("自定义账号密码模式demo")
    public String loginfo() {
        String result = "";
        try {
            //设置header
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", "*/*");
            headers.add("Cache-Control", "no-cache");
            //client_id:client_securt
            headers.add("Authorization","Basic Y2xpZW50X2lkOmNsaWVudF9zZWNyZXQ==");
            //传参数JSON格式
            //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
            MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
            //  也支持中文
            params.add("username", "admin11");
            params.add("password", "123456");
            //设置http请求实体
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);


            result = restTemplate.postForObject("http://localhost:8081/authentication/form", requestEntity, String.class);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return "登陆成功:" + result;
    }

    @RequestMapping(value = "/login_sendSms", method = RequestMethod.POST)
    @ApiOperation("发送验证码")
    public String login_sendSms(
            @RequestParam(value = "mobile", required = false)String mobile
    ) {
        String result = "";
        try {
            //设置header
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", "*/*");
            headers.add("Cache-Control", "no-cache");
            //client_id:client_securt
            headers.add("Authorization","Basic Y2xpZW50X2lkOmNsaWVudF9zZWNyZXQ==");
            //传参数JSON格式
            //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
            MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
            //  也支持中文
            params.add("mobile", "admin");
            //设置http请求实体
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);


            result = restTemplate.postForObject("http://localhost:8080/sms/code", requestEntity, String.class);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return "登陆成功:" + result;
    }
    /**
     * @return
     */
    @RequestMapping(value = "/login_mobileCms", method = RequestMethod.POST)
    @ApiOperation("自定义手机号验证码模式")
    public String login_mobileCms() {
        String result = "";
        try {
            //设置header
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", "*/*");
            headers.add("Cache-Control", "no-cache");
            //client_id:client_securt
            headers.add("Authorization","Basic Y2xpZW50X2lkOmNsaWVudF9zZWNyZXQ==");
            //传参数JSON格式
            //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
            MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
            //  也支持中文
            params.add("mobile", "admin");
            params.add("password", "123456");
            //设置http请求实体
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);


            result = restTemplate.postForObject("http://localhost:8080/authentication/mobile", requestEntity, String.class);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return "登陆成功:" + result;
    }
}
