/**
 * Project Name:midai-pay-common
 * File Name:MidaiLogAutoConfiguration.java
 * Package Name:com.midai.framework.monitor
 * Date:2016年11月14日上午9:45:16
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.framework.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

/**
 * ClassName:MidaiLogAutoConfiguration <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年11月14日 上午9:45:16 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Configuration
public class MidaiLogAutoConfiguration {
	
	@Autowired
	private  Environment environment;
	
	@Bean
	public MidaiLogCommandLineRunner midaiLogCommandLineRunner(){
		MidaiLogCommandLineRunner midaiLogCommandLineRunner=new MidaiLogCommandLineRunner();
		midaiLogCommandLineRunner.setEnableTraceLog(environment.getProperty("midai.log.trace",boolean.class));
		return midaiLogCommandLineRunner;
	}

}

