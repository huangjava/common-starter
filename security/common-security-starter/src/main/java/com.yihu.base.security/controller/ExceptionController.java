package com.yihu.base.security.controller;

import com.yihu.base.security.exception.ApiException;
import com.yihu.base.security.vo.BaseEnvelopStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/4/3 0003.
 */
@RestController
@RequestMapping("/security/exception")
public class ExceptionController {

    /**
     * 密码错误
     */
    @RequestMapping(value = "/badCredentialsException", method = RequestMethod.POST)
    public String badCredentialsException(@RequestParam(value = "msg", required = true) String msg) throws ApiException {

        throw new ApiException(BaseEnvelopStatus.status_10101.getName(), BaseEnvelopStatus.status_10101.getCode());
    }

    /**
     * 用户不存在
     */
    @RequestMapping(value = "/usernameNotFoundException", method = RequestMethod.POST)
    public String usernameNotFoundException(@RequestParam(value = "msg", required = true) String msg) throws ApiException {

        throw new ApiException(BaseEnvelopStatus.status_10100.getName(), BaseEnvelopStatus.status_10100.getCode());
    }

}
