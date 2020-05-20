package com.smc.zuul.feigin;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smc.zuul.bean.AuthRequest;
import com.smc.zuul.bean.ResponseBean;
import com.smc.zuul.bean.Userinfolist;
import com.smc.zuul.utils.CommonResult;


/**
 * @author BoZhang
 * E-mail:dlzbo@cn.ibm.com
 * @version date：May 15, 2020 2:11:29 PM
*/
@FeignClient(value = "SMC-Users")
//@FeignClient(value="eureka-client")
public interface SecurityFeignClient {
	
    // verify admin role
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    ResponseEntity<ResponseBean> isAdmin(@RequestHeader(name = "Authorization") String authHeader);

    // verify token
    @RequestMapping(value = "/authenticated", method = RequestMethod.GET)
    ResponseEntity<ResponseBean> hasToken(@RequestHeader(name = "Authorization") String authHeader);
    
    // generate token
    @RequestMapping(value = "/haslogin", method = RequestMethod.POST)
    ResponseEntity<CommonResult> haslogin(@RequestBody AuthRequest request);
    
    @RequestMapping(value = "/getUser", method = RequestMethod.POST)
    Userinfolist getUserByUsername(@RequestParam("username") String username);
    
    @RequestMapping(value = "/sayhello")
    String sayhello();
}
