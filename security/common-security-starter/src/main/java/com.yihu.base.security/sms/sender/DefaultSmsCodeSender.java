/**
 *
 */
package com.yihu.base.security.sms.sender;

import com.yihu.base.security.exception.ValidateCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenweida
 *         默认的短信发送 没有具体实现
 */
public class DefaultSmsCodeSender implements SmsCodeSender {
    private Logger logger = LoggerFactory.getLogger(DefaultSmsCodeSender.class);

    @Override
    public void send(String mobile, String code) throws ValidateCodeException {
        logger.info("向手机" + mobile + "发送短信验证码" + code);
    }

}
