/**
* @author zhangbo E-mail:dlzbo@cn.ibm.com
* @version date：May 18, 2020 2:17:38 PM
*/
package com.smc.zuul.handler;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * @author BoZhang
 * E-mail:dlzbo@cn.ibm.com
 * @version date：May 18, 2020 2:17:38 PM
*/
@Component
public class SmcAuthenticationEntryPoint implements AuthenticationEntryPoint{
	
	private static Logger logger = LoggerFactory.getLogger(SmcAuthenticationEntryPoint.class);
	
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

    logger.debug("AuthenticationEntryPoint: FOUND 401 Unauthorized");
    // This is invoked when user tries to access a secured REST resource without supplying any credentials
    // We should just send a 401 Unauthorized response because there is no 'login page' to redirect to
    response.sendError(UNAUTHORIZED.value(), UNAUTHORIZED.getReasonPhrase());
    
    }

}
