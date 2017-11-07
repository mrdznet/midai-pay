package com.midai.pay.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebInterceptorConfig extends WebMvcConfigurerAdapter{
	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {		
		MidaiLogWebFilter logFilter=new MidaiLogWebFilter();	
		registry.addInterceptor(logFilter).addPathPatterns("/**");
		super.addInterceptors(registry);
	}

}
