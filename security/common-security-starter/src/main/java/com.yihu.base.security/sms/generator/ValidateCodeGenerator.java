/**
 *
 */
package com.yihu.base.security.sms.generator;

import com.yihu.base.security.sms.vo.ValidateCode;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author chenweida
 *
 */
public interface ValidateCodeGenerator {

	ValidateCode generate(ServletWebRequest request);

}
