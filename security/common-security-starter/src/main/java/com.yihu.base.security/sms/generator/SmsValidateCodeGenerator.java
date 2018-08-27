package com.yihu.base.security.sms.generator;

import com.yihu.base.security.properties.SmsValidateProperties;
import com.yihu.base.security.sms.vo.ValidateCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Random;

/**
 * Created by chenweida on 2017/12/5.
 * 短信验证码生成器
 */
@Component
public class SmsValidateCodeGenerator implements ValidateCodeGenerator {
    @Autowired
    private SmsValidateProperties smsValidateProperties;

    @Override
    public ValidateCode generate(ServletWebRequest request) {
        String code = getFixLenthString(smsValidateProperties.getLength());
        ValidateCode validateCode = new ValidateCode(code, smsValidateProperties.getExpireIn());
        return validateCode;
    }


    /*
 * 返回长度为【strLength】的随机数，在前面补0
 */
    private String getFixLenthString(int strLength) {
        Random rm = new Random();
        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);
        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);

        // 返回固定的长度的随机数
        return fixLenthString.substring(1, strLength + 1);
    }
}
