/**
 * 
 */
package com.yihu.base.security.sms.sender;

import com.yihu.base.security.exception.ValidateCodeException;

/**
 * @author chenweida
 *
 */
public interface SmsCodeSender {
	
	void send(String mobile, String code) throws ValidateCodeException;

}
