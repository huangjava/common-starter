package com.yihu.base.security.rbas.provider;

import com.yihu.base.security.properties.SecurityProperties;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * Created by chenweida on 2017/12/5.
 * 允许通过的路径
 */
@Component
@Order(Integer.MIN_VALUE)
public class PerssionAllAuthorizeConfigProvider implements AuthorizeConfigProvider {


    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry) {

        expressionInterceptUrlRegistry
                .antMatchers(
//                        SecurityProperties.formLogin,//账号密码登录
                        SecurityProperties.formLoginPage,//登录页面
//                        SecurityProperties.mobileLogin,//短信登录
                        SecurityProperties.mobileSendSms,//发送验证码
                        SecurityProperties.login,//登录的所有接口
                        "/swagger-ui.html",//swagger界面所有的接口
                        "/swagger-resources/**",
                        "/v2/api-docs/**",
                        "/configuration/**",
                        "/env/**",
                        "/autoconfig/**",
                        "/features/**",
                        "/health/**",
                        "/logfile/**",
                        "/archaius/**",
                        "/env/**",
                        "/info/**",
                        "/trace/**",
                        "/heapdump/**",
                        "/loggers/**",
                        "/service-registry/**",
                        "/beans/**",
                        "/configprops/**",
                        "/websocket/**",
                        "/sockjs/**",
                        "/dump/**",
                        "/mappings/**",
                        "/refresh/**",
                        "/metrics/**",
                        "/webjars/springfox-swagger-ui/**",
                        "/security/exception/**"//异常处理类
                ).permitAll();
    }
}
