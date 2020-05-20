/**
* @author zhangbo E-mail:dlzbo@cn.ibm.com
* @version date：May 18, 2020 2:15:26 PM
*/
package com.smc.zuul.handler;

import static org.springframework.http.HttpStatus.FORBIDDEN;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;


/**
 * @author BoZhang
 * E-mail:dlzbo@cn.ibm.com
 * @version date：May 18, 2020 2:15:26 PM
*/
@Component
public class SmcAccessDeniedHandler implements AccessDeniedHandler{

	private static Logger logger = LoggerFactory.getLogger(SmcAccessDeniedHandler.class);
	  
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
	
		logger.debug("AccessDeniedHandler: FOUND 403 Forbidden");
		response.sendError(FORBIDDEN.value(), FORBIDDEN.getReasonPhrase());
	    
	}
}
