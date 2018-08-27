/**
 *
 */
package com.yihu.base.security.sms.filter;

import com.yihu.base.security.properties.SecurityProperties;
import com.yihu.base.security.exception.ValidateCodeException;
import com.yihu.base.security.sms.process.SmsValidateCodeProcessor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author chenweida
 */
@Component
public class SmsvalidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

    /**
     * 验证码校验失败处理器
     */
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    /**
     * 验证请求url与配置的url是否匹配的工具类
     */
    private AntPathMatcher pathMatcher = new AntPathMatcher();
    @Autowired
    private SmsValidateCodeProcessor smsValidateCodeProcessor;

    /*
     *   短信验证码登陆过滤器
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if (StringUtils.equalsIgnoreCase(request.getMethod(), "post")) {
            if (pathMatcher.match(SecurityProperties.mobileLogin, request.getRequestURI())) {
                logger.info("校验请求(" + request.getRequestURI() + ")中的验证码");
                try {
                    smsValidateCodeProcessor.validate(new ServletWebRequest(request, response));
                    logger.info("验证码校验通过");
                } catch (ValidateCodeException exception) {
                    authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
                    return;
                }
            }
        }
        chain.doFilter(request, response);

    }


}
