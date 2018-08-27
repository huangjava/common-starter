//package com.demo.service;
//
//import com.yihu.base.security.rbas.ClientServiceProvider;
//import com.yihu.base.security.rbas.UserServiceProvider;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.provider.ClientDetails;
//import org.springframework.security.oauth2.provider.ClientRegistrationException;
//import org.springframework.security.oauth2.provider.client.BaseClientDetails;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//
///**
// * Created by Administrator on 2018/4/3 0003.
// */
//@Component
//public class UserService implements ClientServiceProvider, UserServiceProvider {
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Override
//    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
//        BaseClientDetails baseClientDetails = new BaseClientDetails();
//        baseClientDetails.setClientId("client_id");
//        baseClientDetails.setClientSecret("client_secret");
//        return baseClientDetails;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return new User("admin",
//                    passwordEncoder.encode("123456"),
//                    true,
//                    true,
//                    true,
//                    true
//                    , AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_USER")); //权限
//    }
//}
