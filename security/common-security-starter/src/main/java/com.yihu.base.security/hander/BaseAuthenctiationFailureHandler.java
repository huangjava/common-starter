/**
 *
 */
package com.yihu.base.security.hander;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author chenweida
 */
@Component("BaseAuthenctiationFailureHandler")
public class BaseAuthenctiationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;
//    @Autowired
//    private ExceptionController exceptionController;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException{

        logger.info("登录失败");
        if(exception instanceof UsernameNotFoundException){
            request.getRequestDispatcher("/security/exception/usernameNotFoundException?msg="+exception.getMessage()).forward(request,response);

        }else if(exception instanceof BadCredentialsException){

            request.getRequestDispatcher("/security/exception/badCredentialsException?msg="+exception.getMessage()).forward(request,response);
        }

//        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        response.setContentType("application/json;charset=UTF-8");
//        response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse(exception.getMessage())));
    }

}
