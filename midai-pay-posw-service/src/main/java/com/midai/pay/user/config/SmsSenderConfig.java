package com.midai.pay.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloopen.rest.sdk.CCPRestSDK;
import com.midai.pay.mobile.utils.SmsSenderProperties;
import com.midai.pay.common.utils.SmsSender;

@EnableConfigurationProperties(SmsSenderProperties.class)
@Configuration
public class SmsSenderConfig {
	
	@Autowired
	private SmsSenderProperties smsSenderProperties;
	
	@Bean
	public CCPRestSDK ccpRestSDK(){
		CCPRestSDK ccpRestSDK = new CCPRestSDK();
		return ccpRestSDK;
	}
	
	@Bean
	public SmsSender SmsSender(CCPRestSDK ccpRestSDK){
		SmsSender smsSender = new SmsSender(smsSenderProperties, ccpRestSDK);
		return smsSender;
	}
}
