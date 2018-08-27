package com.yihu.base.security.controller;

import com.yihu.base.security.rbas.ClientServiceProvider;
import com.yihu.base.security.vo.BaseEnvelop;
import com.yihu.base.security.vo.BaseSecurityRequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 刘文彬 on 2018/5/4.
 */
@RestController
@RequestMapping("/tokens")
//@Api(value = "权限token模块", description = "权限token模块")
public class TokenController {

    @Autowired
    private DefaultTokenServices defaultTokenServices;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ClientServiceProvider clientDetailsService;
//    @Autowired
//    private EmployFeign employFeign;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @PostMapping(BaseSecurityRequestMapping.BaseToken.api_update_token_expiration_time)
//    @ApiOperation(value = "更新token过期时间", notes = "根据token更新token过期时间")
    public BaseEnvelop updateTokenExpiration(
//            @ApiParam(name = "expiration", value = "token过期时间，格式：yyyy-mm-dd HH:mm:ss", required = true)
                     @RequestParam(value = "expiration", required = true) String expiration,
//                                             @ApiParam(name = "authHeaderValue", value = "登录的token", required = true)
                     @RequestParam(value = "authHeaderValue", required = true) String authHeaderValue) throws ParseException {

        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken)defaultTokenServices.readAccessToken(authHeaderValue);
        if(token!=null){
            try {
                token.setExpiration(formatter.parse(expiration));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return BaseEnvelop.getSuccess("token 过期时间设置成功！");
        }
        return BaseEnvelop.getError("token 无效！");
    }

    @PostMapping(BaseSecurityRequestMapping.BaseToken.api_update_token_expiration_second)
//    @ApiOperation(value = "延长token的过期时间", notes = "根据token延长token的过期时间")
    public BaseEnvelop updateTokenExpiration(
//            @ApiParam(name = "seconds", value = "延长token过期时间分钟数", required = true)
                                             @RequestParam(value = "seconds", required = true) int seconds,
//                                             @ApiParam(name = "authHeaderValue", value = "登录的token", required = true)
                                             @RequestParam(value = "authHeaderValue", required = true) String authHeaderValue){

        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken)defaultTokenServices.readAccessToken(authHeaderValue);
        if(token!=null){
            token.setExpiration(new Date(System.currentTimeMillis() + (seconds * 1000L)));
            return BaseEnvelop.getSuccess("token 过期时间设置成功！");
        }
        return BaseEnvelop.getError("token 无效！");
    }

    @PostMapping(BaseSecurityRequestMapping.BaseToken.api_update_token_expiration)
//    @ApiOperation(value = "设置token过期", notes = "设置token过期")
    public BaseEnvelop updateTokenExpiration2(HttpServletRequest request, HttpServletResponse response,
//                                              @ApiParam(name = "authHeaderValue", value = "登录的token", required = true)
                                              @RequestParam(value = "authHeaderValue", required = true) String authHeaderValue){

        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken)defaultTokenServices.readAccessToken(authHeaderValue);
        if(token!=null){
            token.setExpiration(new Date());
            return BaseEnvelop.getSuccess("token 过期时间设置成功！");
        }
        return BaseEnvelop.getError("token 无效！");
    }

//    @PostMapping(BaseSecurityRequestMapping.BaseToken.api_update_token_expiration_second2)
//    @ApiOperation(value = "设置token过期", notes = "设置token过期")
//    public BaseEnvelop updateTokenExpiration3(HttpServletRequest request, HttpServletResponse response,
//                                              @ApiParam(name = "mobileSaas", value = "mobile和saas组合，逗号分隔", required = true)
//                                              @RequestParam(value = "mobileSaas", required = true) String mobileSaas){
//
//        String[] sp = mobileSaas.split(",");
//        String phone = sp[0];
//        String saasId = sp[1];
//        Envelop envelop = employFeign.getEmployeeByPhoneAndSaasId(phone,saasId);
//        Map baseEmployDO =  (Map)envelop.getObj();
//        if(baseEmployDO.isEmpty()){
//            return BaseEnvelop.getError("该用户不存在！");
//        }
//        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
//                mobileSaas, baseEmployDO.get("password"));
//
//        // Allow subclasses to set the "details" property
//        authRequest.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//        Authentication authentication = authenticationManager.authenticate(authRequest);
//        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(saasId);
//        TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP, saasId, clientDetails.getScope(), "custom_password");
//
//        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
//
//        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
//
//        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken)defaultTokenServices.getAccessToken(oAuth2Authentication);
//        if(token!=null){
//            token.setExpiration(new Date());
//            return BaseEnvelop.getSuccess("token 过期时间设置成功！");
//        }
//        return BaseEnvelop.getError("token 无效！");
//    }
}
