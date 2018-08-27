package com.yihu.base.security.sms.process;

import com.yihu.base.security.properties.SecurityProperties;
import com.yihu.base.security.exception.ValidateCodeException;
import com.yihu.base.security.sms.generator.SmsValidateCodeGenerator;
import com.yihu.base.security.sms.sender.SmsCodeSender;
import com.yihu.base.security.sms.vo.ValidateCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import net.sf.json.JSONObject;

/**
 * @author chenweida
 */
@Component
public class SmsValidateCodeProcessor implements ValidateCodeProcessor {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private SmsCodeSender smsCodeSender;
    @Autowired
    private SmsValidateCodeGenerator smsValidateCodeGenerator;

    /*
     *  生成验证码
     */
    @Override
    public void create(ServletWebRequest request) throws Exception {
        //生成校验码
        ValidateCode validateCode = generate(request);
        //保存校验码
        save(request, validateCode);
        //发送校验码到手机号
        send(request, validateCode);
    }

    /**
     * 生成校验码
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    public ValidateCode generate(ServletWebRequest request) {
        return smsValidateCodeGenerator.generate(request);
    }

    /**
     * 保存校验码
     *
     * @param request
     * @param validateCode
     */
    public void save(ServletWebRequest request, ValidateCode validateCode) {
        JSONObject jo = new JSONObject();
        jo.put("code", validateCode.getCode());//保存验证码
        jo.put("expireTime", validateCode.getExpireTimeString()); //保存超时时间
        jo.put("createTime", validateCode.getCreateTimeString()); //保存超时时间
        redisTemplate.opsForValue().set(key(request), jo.toString());
    }

    /**
     * 删除严验证码
     *
     * @param request
     */
    public void reomve(ServletWebRequest request) {
        redisTemplate.delete((key(request)));
    }

    /**
     * 获取校验码
     *
     * @param request
     */
    private ValidateCode get(ServletWebRequest request) {
        String joStr = redisTemplate.opsForValue().get(key(request));
        if (StringUtils.isNotBlank(joStr)) {
            JSONObject jo = JSONObject.fromObject(joStr);
            ValidateCode validateCode = new ValidateCode();
            validateCode.setCode(jo.getString("code"));
            validateCode.setExpireTimeString(jo.getString("expireTime"));
            validateCode.setCreateTimeString(jo.getString("createTime"));
            return validateCode;
        } else {
            return null;
        }
    }

    /**
     * 拼凑放在redis的key  格式 security:oauth2:smsLogin:{手机号}
     *
     * @param request
     * @return
     */
    private String key(ServletWebRequest request) {
        return SecurityProperties.prefix_sms + request.getParameter(SecurityProperties.mobileLoginAccountKey);
    }

    /**
     * 发送校验码，由子类实现
     *
     * @param request
     * @param validateCode
     * @throws Exception
     */
    protected void send(ServletWebRequest request, ValidateCode validateCode) throws Exception {
        smsCodeSender.send(request.getParameter(SecurityProperties.mobileLoginAccountKey), validateCode.getCode());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void validate(ServletWebRequest request) {
        //获取验证码
        ValidateCode validateCode = get(request);

        if (validateCode == null) {
            throw new ValidateCodeException("验证码不存在");
        }
        String codeInRequest;
        //获取请求中的验证码
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),
                    SecurityProperties.mobileLoginSmsKey);
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }


        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }


        if (validateCode.isExpried()) {
            throw new ValidateCodeException("验证码已过期");
        }

        if (!StringUtils.equals(validateCode.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }
    }

}
