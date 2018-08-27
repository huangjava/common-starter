package com.yihu.base.security.hander;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yihu.base.security.vo.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.web.authentication.logout.CompositeLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 刘文彬 on 2018/5/3.
 */
@Component("baseLogoutSuccessHandler")
public class BaseLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    @Autowired
    private DefaultTokenServices defaultTokenServices;
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        super.onLogoutSuccess(request, response, authentication);
        String header = request.getHeader("Authorization");
        if(!StringUtils.isEmpty(header)){
            if ((header.toLowerCase().startsWith(OAuth2AccessToken.BEARER_TYPE.toLowerCase()))) {
                String authHeaderValue = header.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
                //删除OAuth2Authentication
                OAuth2Authentication authen =  defaultTokenServices.loadAuthentication(authHeaderValue);
                if(authen!=null){
                    new CompositeLogoutHandler(new SecurityContextLogoutHandler()).logout(request,response,authen);
                    //删除redis中的token
                    if(defaultTokenServices.revokeToken(authHeaderValue)){
                        return ;
                    }
                }
                logger.info("登出失败");
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse("logout failure cause by redis delete token fail!")));
            }
        }
    }

}
