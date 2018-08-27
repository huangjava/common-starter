package com.yihu.base.security.config;

import com.yihu.base.security.properties.SecurityProperties;
import com.yihu.base.security.rbas.UserServiceProvider;
import com.yihu.base.security.rbas.provider.AuthorizeConfigProviderManager;
import com.yihu.base.security.rbas.provider.UserNamePasswordAuthenticationProvider;
import com.yihu.base.security.sms.SmsCodeAuthenticationSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.inject.Inject;

/**
 * Created by chenweida on 2017/12/4.
 */

@Configuration
@EnableResourceServer  //开启资源服务器
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    protected AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    protected AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private OAuth2AuthenticationManager authenticationManager;
    @Autowired
    private TokenStore redisTokenStore;
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
    @Autowired
    private AuthorizeConfigProviderManager authorizeConfigProviderManager;
    @Autowired
    private OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler;
    @Autowired
    private LogoutSuccessHandler baseLogoutSuccessHandler;
    @Autowired
    private UserServiceProvider userServiceProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                    .csrf().disable()
                .formLogin()//设置 账号密码登陆
                    .loginPage(SecurityProperties.formLoginPage)
                    .loginProcessingUrl(SecurityProperties.formLogin)
                    .usernameParameter("username")//默认就是username
                    .passwordParameter("password")//默认就是password
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                .and()
                    .logout().logoutUrl(SecurityProperties.formLoginout).logoutSuccessUrl("/")
                    .logoutSuccessHandler(baseLogoutSuccessHandler)
                .and()
                    .apply(smsCodeAuthenticationSecurityConfig); //添加自定义短信登陆;


        http.authenticationProvider(getMyAuthenticationProvider());
        //验证路径
        authorizeConfigProviderManager.config(http.authorizeRequests());
    }

    private AuthenticationProvider getMyAuthenticationProvider(){
        UserNamePasswordAuthenticationProvider userNamePasswordAuthenticationProvider = new UserNamePasswordAuthenticationProvider();
        userNamePasswordAuthenticationProvider.setUserDetailsService(userServiceProvider);
        userNamePasswordAuthenticationProvider.setHideUserNotFoundExceptions(false);
        userNamePasswordAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return userNamePasswordAuthenticationProvider;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.
                authenticationManager(authenticationManager)
                .tokenStore(redisTokenStore)
                .expressionHandler(oAuth2WebSecurityExpressionHandler);
    }

    /**
     * 解决bug
     * Failed to evaluate expression '#oauth2.throwOnError
     * No bean resolver registered in the context to resolve access to bean
     * @param applicationContext
     * @return
     */
    @Bean
    @Primary
    public OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler(ApplicationContext applicationContext) {
        OAuth2WebSecurityExpressionHandler expressionHandler = new OAuth2WebSecurityExpressionHandler();
        expressionHandler.setApplicationContext(applicationContext);
        return expressionHandler;
    }

    @Bean
    @ConditionalOnMissingClass
    public ReflectionSaltSource reflectionSaltSource() {
        ReflectionSaltSource reflectionSaltSource=new ReflectionSaltSource();
        reflectionSaltSource.setUserPropertyToUse("getSalt");
        return reflectionSaltSource;
    }

    @Inject
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setHideUserNotFoundExceptions(false);
        provider.setUserDetailsService(userServiceProvider);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setSaltSource(reflectionSaltSource());
        return provider;
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//
//        auth.parentAuthenticationManager(new ProviderManager(new ArrayList<>(Arrays.asList(authenticationProvider()))));
//    }

}
