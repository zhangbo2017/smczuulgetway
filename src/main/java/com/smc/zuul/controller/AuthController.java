/**
* @author zhangbo E-mail:dlzbo@cn.ibm.com
* @version date：May 18, 2020 4:05:38 PM
*/
package com.smc.zuul.controller;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import static org.springframework.http.HttpStatus.OK;

import com.smc.zuul.utils.ResponseBean;
import com.smc.zuul.bean.AuthRequest;
import com.smc.zuul.bean.AuthResponse;
import com.smc.zuul.bean.Const;
import com.smc.zuul.bean.Userinfolist;
import com.smc.zuul.feigin.SecurityFeignClient;
import com.smc.zuul.filters.SmcUserDetailsService;
import com.smc.zuul.utils.CommonResult;
import com.smc.zuul.utils.JwtTokenUtil;

/**
 * @author BoZhang
 * E-mail:dlzbo@cn.ibm.com
 * @version date：May 18, 2020 4:05:38 PM
*/
@CrossOrigin
@RestController
public class AuthController {
  @Autowired
  private AuthenticationManager authenticationManager;
	
  @Autowired
  private SecurityFeignClient securityFeignClient;
  
  @Autowired
  private SmcUserDetailsService smcUserDetailsService;
	
  @RequestMapping(value="/login")
  public ResponseEntity<CommonResult> login(@RequestBody AuthRequest request) throws Exception {
	Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
	SecurityContextHolder.getContext().setAuthentication(authentication);
	HttpStatus authChkStatus=this.securityFeignClient.haslogin(request).getStatusCode();
	if(authChkStatus.equals(OK)) {
		UserDetails userDetails=smcUserDetailsService.loadUserByUsername(request.getUsername());
	    String jwtToken = JwtTokenUtil.generateToken(userDetails, false);
	    System.out.println("jwtToken >>>>"+jwtToken);
	    
	    AuthResponse authResponse = new AuthResponse();
	    // authResponse.setUsername(request.getUsername());
	    authResponse.setUsername(userDetails.getUsername());
	    Set<GrantedAuthority> authorities = (Set<GrantedAuthority>) userDetails.getAuthorities();
	    authResponse.setUsertype(authorities.toArray()[0].toString());
	    authResponse.setJwtToken(jwtToken);
	    
	    return ResponseEntity.ok().body(CommonResult.build(Const.COMMONRESULT_OK_CODE, "Login successfully!", authResponse));
		
	}else {
		return ResponseEntity.ok().body(CommonResult.build(authChkStatus.value(), "Login Failed!"));
	}
	
  }

  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(UNAUTHORIZED)
  public ResponseEntity<ResponseBean> handleAuthentication401Exception(AuthenticationException exception) throws Exception {
    return ResponseEntity.status(UNAUTHORIZED)
    	.body(new ResponseBean(UNAUTHORIZED.value(), UNAUTHORIZED.getReasonPhrase()).error(exception.getMessage()));
  }

  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(FORBIDDEN)
  public ResponseEntity<ResponseBean> handleAuthentication403Exception(AuthenticationException exception) throws Exception {
    return ResponseEntity.status(FORBIDDEN)
    	.body(new ResponseBean(FORBIDDEN.value(), FORBIDDEN.getReasonPhrase()).error(exception.getMessage()));
  }

}
