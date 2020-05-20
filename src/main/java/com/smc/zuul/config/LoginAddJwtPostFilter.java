///**
//* @author zhangbo E-mail:dlzbo@cn.ibm.com
//* @version date：May 19, 2020 8:40:27 PM
//*/
//package com.smc.zuul.config;
//
//import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;
//
//import java.io.InputStream;
//import java.nio.charset.Charset;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StreamUtils;
//
//import com.github.andrewoma.dexx.collection.Map;
//import com.google.gson.Gson;
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import com.netflix.zuul.exception.ZuulException;
//import com.smc.zuul.feigin.SecurityFeignClient;
//import com.smc.zuul.filters.SmcUserDetailsService;
//
///**
// * @author BoZhang
// * E-mail:dlzbo@cn.ibm.com
// * @version date：May 19, 2020 8:40:27 PM
//*/
//@Component
//public class LoginAddJwtPostFilter extends ZuulFilter {
//	  private static Logger log = LoggerFactory.getLogger(LoginAddJwtPostFilter.class);
//	  private static final String LOGIN_URI = "/login"; // permit
//	  
//	  @Autowired
//	  private SecurityFeignClient securityFeignClient;
//	  @Autowired
//	  private SmcUserDetailsService smcUserDetailsService;
//	
//	  @Override
//	  public String filterType() {
//	    return POST_TYPE;
//	  }
//
//	/* (non-Javadoc)
//	 * @see com.netflix.zuul.IZuulFilter#shouldFilter()
//	 */
//	@Override
//	public boolean shouldFilter() {
//		// TODO Auto-generated method stub
//		
//		RequestContext requestContext = RequestContext.getCurrentContext();
//	    HttpServletRequest request = requestContext.getRequest();
//
//	    if (request.getRequestURI().indexOf(LOGIN_URI) >= 0) {
//	      log.debug("POSTRequestFilter-getRequestURI: {}", request.getRequestURI());
//	      System.out.println("POSTRequestFilter-getRequestURI: {} >>>" + request.getRequestURI());
//	      return false;
//	    }
//	    return true;
//	}
//
//	/* (non-Javadoc)
//	 * @see com.netflix.zuul.IZuulFilter#run()
//	 */
//	@Override
//	public Object run() throws ZuulException {
//		System.out.print(securityFeignClient.sayhello());
//
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	/* (non-Javadoc)
//	 * @see com.netflix.zuul.ZuulFilter#filterOrder()
//	 */
//	@Override
//	public int filterOrder() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//}
