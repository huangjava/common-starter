package com.yihu.base.security.rbas;


import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenweida on 2017/12/1.
 * 需要权限认证的使用这个接口
 */
public interface RbasServiceProvider {

     Boolean hasPerssion(HttpServletRequest request, Authentication authentication) ;
}
