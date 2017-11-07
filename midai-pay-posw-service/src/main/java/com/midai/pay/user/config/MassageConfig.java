package com.midai.pay.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.midai.pay.mobile.utils.MassageProperties;
import com.midai.pay.mobile.utils.MassageUtil;

@EnableConfigurationProperties(MassageProperties.class)
@Configuration
public class MassageConfig {
	
	@Autowired
	private MassageProperties msgProperties;
	
	@Bean
	public MassageUtil msgUtil(){
		MassageUtil msgUtil = new MassageUtil(msgProperties);
		
		return msgUtil;
	}
}
