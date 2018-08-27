package com.yihu.base.security.sms.controller;

import com.yihu.base.security.properties.SecurityProperties;
import com.yihu.base.security.sms.mobile.MobileCheck;
import com.yihu.base.security.sms.process.SmsValidateCodeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by chenweida on 2017/12/5.
 */
@RestController
public class SmsController {
    @Autowired
    private SmsValidateCodeProcessor smsValidateCodeProcessor;
    @Autowired
    private MobileCheck mobileCheck;

    /**
     * 创建验证码
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping(SecurityProperties.mobileSendSms)
    public void createCode(
            HttpServletRequest request,
            HttpServletResponse response)
            throws Exception {
        //获取手机号
        String mobile = request.getParameter(SecurityProperties.mobileLoginAccountKey);
        //验证手机号是否正确
        if (!mobileCheck.checkMobile(mobile)) {
            //通过设置响应头控制浏览器以UTF-8的编码显示数据，如果不加这句话，那么浏览器显示的将是乱码
            response.setHeader("content-type", "text/html;charset=UTF-8");
            response.setStatus(HttpStatus.NOT_IMPLEMENTED.value());//参数错误
            PrintWriter pw = response.getWriter();
            pw.write(new String("{\"content\":\"电话号码格式错误\"}"));
            pw.flush();
        } else {
            //发送短信验证码并且保存到redis中
            smsValidateCodeProcessor.create(new ServletWebRequest(request, response));
        }
    }


}
