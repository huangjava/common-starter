package com.yihu.base.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yihu.base.security.properties.AccessTokenPorperties;
import com.yihu.base.security.properties.SecurityProperties;
import com.yihu.base.security.rbas.ClientServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * Created by chenweida on 2017/12/4.
 */
@Configuration
@EnableAuthorizationServer  //开启授权服务器
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ClientServiceProvider clientDetailsService;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccessTokenPorperties accessTokenPorperties;


    //用来配置令牌端点(Token Endpoint)的安全约束
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.passwordEncoder(passwordEncoder);

    }

    //用来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)。
    //认证管理器，当你选择了资源所有者密码（password）授权类型的时候，请设置这个属性注入一个 AuthenticationManager 对象。
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(oAuth2AuthenticationManager())
                .tokenStore(tokenStore())
                .userDetailsService(userDetailsService)
                .tokenServices(defaultTokenServices())
//        .authorizationCodeServices()//用来授权码服务的
               // .pathMapping("/oauth/confirm_access", "/extenal/oauth/confirm_access");//授权码模式  授权页面转换

        ;

        //endpoints.setClientDetailsService(clientDetailsService);

    }

    //用来配置客户端详情服务
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //.jdbc(dataSource).passwordEncoder(passwordEncoder) .clients(clientDetailsService)

        clients.withClientDetails(clientDetailsService);
        ;
    }


    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper;
    }

    @Bean
    @Primary
    OAuth2AuthenticationManager oAuth2AuthenticationManager() {
        OAuth2AuthenticationManager oAuth2AuthenticationManager = new OAuth2AuthenticationManager();
        oAuth2AuthenticationManager.setClientDetailsService(clientDetailsService);
        oAuth2AuthenticationManager.setTokenServices(defaultTokenServices());
        return oAuth2AuthenticationManager;
    }

    //==========================token相关配置=================================
    @Bean
    @Primary
    DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setAccessTokenValiditySeconds(60 * 60 * accessTokenPorperties.getAccessTokenValidityHours()); //默认2小时
        defaultTokenServices.setRefreshTokenValiditySeconds(60 * 60 * accessTokenPorperties.getRefreshTokenValidityHours());//默认2小时
        defaultTokenServices.setClientDetailsService(clientDetailsService);
        return defaultTokenServices;
    }

    @Bean
    @Primary
    TokenStore tokenStore() {
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setPrefix(SecurityProperties.prefix_accesstoken);
        return redisTokenStore;
    }
}
