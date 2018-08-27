/**
 *
 */
package com.yihu.base.security.hander;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yihu.base.security.properties.SecurityProperties;
import com.yihu.base.security.rbas.ClientServiceProvider;
import com.yihu.base.security.sms.process.SmsValidateCodeProcessor;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author chenweida
 *         <p>
 *         账号密码提交需要在 head 中添加 Basic clientID:cliengSecurty
 *         在成功的处理器中返回token，spring security默认在TokenEndpoint中获取token
 */
@Component("BaseAuthenticationSuccessHandler")
public class BaseAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 验证请求url与配置的url是否匹配的工具类
     */
    private AntPathMatcher pathMatcher = new AntPathMatcher();
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ClientServiceProvider clientDetailsService;
    @Autowired
    private AuthorizationServerTokenServices defaultTokenServices;
    @Autowired
    private SmsValidateCodeProcessor smsValidateCodeProcessor;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /*
         * (non-Javadoc)
         *
         * @see org.springframework.security.web.authentication.
         * AuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.
         * HttpServletRequest, javax.servlet.http.HttpServletResponse,
         * org.springframework.security.core.Authentication)
         */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if (org.springframework.util.StringUtils.isEmpty(header) || (!header.startsWith("Basic "))) {
            throw new UnapprovedClientAuthenticationException("请求头没有client信息");
        }
        //解析头部的basic信息
        String[] tokens = extractAndDecodeHeader(header, request);
        assert tokens.length == 2;

        String clientId = tokens[0];
        String clientSecurity =tokens[1];
        //得到ClientDetails
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

        if (clientDetails == null) {
            throw new UnapprovedClientAuthenticationException("clientId不存在 client:" + clientId);
        } else if (!StringUtils.equals(clientSecurity,clientDetails.getClientSecret())) {
            throw new UnapprovedClientAuthenticationException("clientSecurity 不匹配 client:" + clientId);
        }

        TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP, clientId, clientDetails.getScope(), "custom_password");

        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);

        OAuth2AccessToken token = defaultTokenServices.createAccessToken(oAuth2Authentication);

        if(pathMatcher.match(SecurityProperties.mobileLogin, request.getRequestURI())){
            //验证码模式登陆，说明登陆成功  删除验证码
            smsValidateCodeProcessor.reomve(new ServletWebRequest(request,response));
        }
        if(token==null){

        }
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(token));

    }


    /**
     * 解析
     *
     * @param header
     * @param request
     * @return
     * @throws IOException
     */
    private String[] extractAndDecodeHeader(String header, HttpServletRequest request)
            throws IOException {

        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException(
                    "Failed to decode basic authentication token");
        }

        String token = new String(decoded, "UTF-8");

        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new BadCredentialsException("Basic 信息不合法");
        }
        return new String[]{token.substring(0, delim), token.substring(delim + 1)};
    }

}
