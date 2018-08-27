**授权码模式:(一般开放API用)**

注:
    1. 获取客户端的实现逻辑结合自己的客户端用户体系实现 （com.yihu.base.security.rbas.ClientServiceProvider）
第一步
获取code
http://localhost:8060/oauth/authorize?response_type=code&client_id=cwd&redirect_uri=http://example.com&scope=app
参数说明：
response_type=code 固定
scope=app 固定
client_id=cwd 根据用户表中自己定义的填写
redirect_uri=http://example.com 根据用户表中自己定义的填写

第二步 
获取token post请求
http://localhost:8060/oauth/token
header:  Basic {appid}:{appsecuri} 加密  例如 Basic Y3dkOmN3ZA==
  
{
     "grant_type":"authorization_code", 授权模式固定
     "client_id":"cwd",
     "code":"第一步请求获取的code",
     "redirect_uri":"http://example.com",
     "scope":"app"
}
返回值
{
    "access_token":"bd677e24-2de5-4862-a5e1-8f90a074db42",   默认2小时过期时间 可以配置 ，由于每次请求都需要验证access_token，所以access_token存储在redis
    "token_type":"bearer",
    "refresh_token":"1427b997-ef94-4061-8940-c71da6549acd",  默认2小时过期时间 可以配置 
    "expires_in":43199,
    "scope":"app"
}

**密码模式（一般自己公司系统用）**

注:  
    1. 获取用户的实现逻辑结合自己的用户体系实现 （org.springframework.security.core.userdetails.UserDetailsService）
    
获取token post请求
http://localhost:8060/oauth/token
header:  Basic {appid}:{appsecuri} 加密  例如 Basic Y3dkOmN3ZA==
  
{
     "grant_type":"password",  
     "username":"admin",
     "password":"123456",
     "scope":"app"
}

返回值
{
    "access_token":"630e2ccc-a5ce-4486-a855-ba755eb3d0d2",
    "token_type":"bearer",
    "refresh_token":"bbb36b54-61b2-4d86-aed3-91c5135174c3",
    "expires_in":43199,
    "scope":"app"
}


**刷新token**
获取token post请求
http://localhost:8060/oauth/token
header:  Basic {appid}:{appsecuri} 加密  例如 Basic Y3dkOmN3ZA==
  
{
     "grant_type":"refresh_token",
     "refresh_token":"bbb36b54-61b2-4d86-aed3-91c5135174c3"
}

返回值
{
    "access_token":"630e2ccc-a5ce-4486-a855-ba755eb3d0d2",
    "token_type":"bearer",
    "refresh_token":"bbb36b54-61b2-4d86-aed3-91c5135174c3",
    "expires_in":43199,
    "scope":"app"
}



**自定义账号密码登陆**
POST
http://localhost:8060/authentication/form

header:  Basic {appid}:{appsecuri} 加密  例如 Basic Y3dkOmN3ZA==

注:  
    1. 获取用户的实现逻辑结合自己的用户体系实现 （org.springframework.security.core.userdetails.UserDetailsService）
body
{ 
    "username":"test",
    "password":"123456"
}
返回值
{
    "access_token":"630e2ccc-a5ce-4486-a855-ba755eb3d0d2",
    "token_type":"bearer",
    "refresh_token":"bbb36b54-61b2-4d86-aed3-91c5135174c3",
    "expires_in":43199,
    "scope":"all"
}
{
    "access_token":"4ce54971-91f9-4c19-bf76-a3c1d3e8c495",
    "token_type":"bearer",
    "refresh_token":"37e3e3bc-8897-4eb4-b43b-4b8382f0efdf",
    "scope":"app"
}

**自定义手机号短信验证码登陆**

获取短信
注:  
    1. 短信验证码默认存在redis中(不可配置)
   2. 短信超时时间1分钟(可配置)
   3. 手机号码验证规则可自定义(实现接口com.yihu.base.security.sms.mobile.MobileCheck)
   4. 短信发送器必须自己实现(实现接口com.yihu.base.security.sms.sender.SmsCodeSender)
GET
http://localhost:8060/code/sms

body
{
    "mobile":"13612345678"
}


POST
http://localhost:8060/authentication/mobile
注:  
    1. 短信登陆成功验证码会删除

header:  Basic {appid}:{appsecuri} 加密  例如 Basic Y3dkOmN3ZA==

body
{
    "mobile":"test",
    "sms":"246053"
}

返回值
{
    "access_token":"630e2ccc-a5ce-4486-a855-ba755eb3d0d2",
    "token_type":"bearer",
    "refresh_token":"bbb36b54-61b2-4d86-aed3-91c5135174c3",
    "expires_in":43199,
    "scope":"app"
}

**访问方式**
http://localhost:8060/user
header 
{
"Authorization":"bearer 5fe6b2c3-f69c-4ddc-a36a-367cdf9479a3"      即 bearer accesstoken
}
