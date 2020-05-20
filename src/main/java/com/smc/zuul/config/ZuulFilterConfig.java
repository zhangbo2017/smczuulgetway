package com.smc.zuul.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author BoZhang
 * E-mail:dlzbo@cn.ibm.com
 * @version date：May 15, 2020 2:12:22 PM
*/
@Configuration
public class ZuulFilterConfig {
	
	  @Bean
	  public PreFilter preFilter() {
		  return new PreFilter();
	  }

}
