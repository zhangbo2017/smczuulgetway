
package com.smc.zuul.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.smc.zuul.bean.Userinfolist;
import com.smc.zuul.feigin.SecurityFeignClient;
import com.smc.zuul.filters.JwtAuthenticationTokenFilter;
import com.smc.zuul.utils.JwtTokenUtil;

import io.jsonwebtoken.Claims;

/**
 * @author BoZhang
 * E-mail:dlzbo@cn.ibm.com
 * @version date：May 18, 2020 2:22:56 PM
*/

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter{

	  private static Logger logger = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);
	  
	  @Autowired
	  private UserDetailsService userDetailsService;
	  
	  @Autowired
	  private SecurityFeignClient securityFeignClient;
	  
	  @Autowired
	  private SmcUserDetailsService smcUserDetailsService;

	  @Override
	  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	      throws ServletException, IOException {
		  System.out.print("doFilterInternal");
	    String authToken = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
	    if (authToken != null && authToken.startsWith(JwtTokenUtil.TOKEN_PREFIX)) {
	      authToken = authToken.substring(JwtTokenUtil.TOKEN_PREFIX.length());
	      if (authToken.equals("null")) {
		      filterChain.doFilter(request, response);
		      return;
		    }
	      logger.debug("JwtAuthenticationTokenFilter - authTokenHeader = {}", authToken);
	    } else {
	      authToken = request.getParameter("JWT-Tonken");
	      logger.debug("JwtAuthenticationTokenFilter - authTokenParams = {}" + authToken);

	      if (authToken == null) {
	    	  System.out.print("I am good");
	        filterChain.doFilter(request, response);
	        return;
	      }
	    }

	    try {
	      String username = JwtTokenUtil.getUsername(authToken); // if token invalid, will get exception here
	      
	      // add start
	      Claims claims = JwtTokenUtil.getTokenBody(authToken);
	      if(claims == null ){
	      	filterChain.doFilter(request, response);
	      	return;
	      }else{
	    	  Userinfolist users = securityFeignClient.getUserByUsername(username);
	          if(JwtTokenUtil.isTokenExpired(claims.getExpiration(), users.getLastupdate(), claims.getIssuedAt())){
	          	filterChain.doFilter(request, response); 
	          	return;
	         }
	      }
	      // add end

	      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	        logger.debug("JwtAuthenticationTokenFilter: checking authentication for user = {}", username);
	        UserDetails userDetails = smcUserDetailsService.loadUserByUsername(username);
	        if (JwtTokenUtil.validateToken(authToken, userDetails)) {
	          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), "N/A",
	                                                                                                       userDetails.getAuthorities());
	          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	          SecurityContextHolder.getContext().setAuthentication(authentication);
	        }else{
	        	request.setAttribute(authToken, null);
	        }
	      }
	    } catch (Exception e) {
	     logger.debug("JwtAuthenticationTokenFilter:Exception");
	     logger.error(e.getMessage(), e);
	    }

	    filterChain.doFilter(request, response);
	  }
}
