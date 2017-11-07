package com.midai.pay.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jianzhou.sdk.BusinessService;
import com.midai.pay.mobile.utils.SmsProperties;
import com.midai.pay.mobile.utils.SmsUtil;

@EnableConfigurationProperties(SmsProperties.class)
@Configuration
public class SmsConfig {
	
	@Autowired
	private SmsProperties smsProperties;
	
	@Bean
	public BusinessService businessService(){
		BusinessService businessService=new BusinessService();
		return businessService;
	}
	
	@Bean
	public SmsUtil smsUtil(BusinessService businessService){
		SmsUtil smsUtil=new SmsUtil(smsProperties, businessService);
		return smsUtil;
	}


}
