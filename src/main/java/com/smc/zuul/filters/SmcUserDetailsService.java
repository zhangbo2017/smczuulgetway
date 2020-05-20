/**
* @author zhangbo E-mail:dlzbo@cn.ibm.com
* @version date：May 18, 2020 9:28:17 PM
*/
package com.smc.zuul.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.smc.zuul.bean.Userinfolist;
import com.smc.zuul.feigin.SecurityFeignClient;

/**
 * @author BoZhang
 * E-mail:dlzbo@cn.ibm.com
 * @version date：May 18, 2020 9:28:17 PM
*/
@Service
public class SmcUserDetailsService implements UserDetailsService{
	
	  @Autowired
	  private SecurityFeignClient securityFeignClient;

	  @Override
	  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    Userinfolist users = securityFeignClient.getUserByUsername(username);
	    if (users == null) {
	      throw new UsernameNotFoundException("USERNAME NOT FOUND");
	    }
	    String password = users.getPassword();
	    String role = users.getUsertype();
	    Boolean userDisabled = false;
	    if (!users.getConfirmed().equalsIgnoreCase("1")) {
	      userDisabled = true;
	    }
	    return User.withUsername(username).password(new BCryptPasswordEncoder().encode(password)).disabled(userDisabled).roles(role).build();
	  }


}
