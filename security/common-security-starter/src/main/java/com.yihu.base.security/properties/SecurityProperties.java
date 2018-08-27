package com.yihu.base.security.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chenweida on 2017/12/4.\
 * 安全框架配置信息
 */
public class SecurityProperties {
    //放在redis中的前缀
    public final static String prefix_accesstoken = "security:oauth2:";  //oauth2 的前缀
    public final static String prefix_sms = "security:oauth2:smsLogin:";  //短信验证码的前缀

    //表单登陆相关信息
    public final static String formLogin = "/authentication/form";//账号密码的路径
    public final static String formLoginPage = "/denglu.html";
    public final static String formLoginout = "/logout";
    //短信登陆相关信息
    public final static String mobileLogin = "/authentication/mobile"; //手机号短信登陆的路径
    public final static String mobileLoginAccountKey = "mobile";
    public final static String mobileLoginSmsKey = "sms";

    public final static String mobileSendSms = "/code/sms";     //发送短信接口的路径
    public final static String login = "/svr-bases/gateway/login/**";//登录的所有接口路径
}
