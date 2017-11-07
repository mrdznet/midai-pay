/**
 * Project Name:spirng-boot-starter-dubbo
 * File Name:DubboAutoConfiguration.java
 * Package Name:com.midai.springboot.dubbo
 * Date:2016年7月26日下午5:44:09
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.framework.config.dubbo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;

/**
 * ClassName:DubboAutoConfiguration <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年7月26日 下午5:44:09 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Configuration
@ConditionalOnClass(LoggerFactory.class)
@Order
public class DubboAutoConfiguration {

	protected Logger logger=LoggerFactory.getLogger(getClass());
	
	@Value("${shutdown.latch.domain.name: com.midai.dubbo.management}")
	private String shutdownLatchDomainName;
	
	@Bean
	@ConditionalOnClass(name="com.alibaba.dubbo.rpc.Exporter")
	public DubboServiceLatchCommandLineRunner configureDubboServiceLatchCommandLineRunner(){
		DubboServiceLatchCommandLineRunner runner=new DubboServiceLatchCommandLineRunner();
		logger.debug("DubboAutoConfiguration enabled by adding DubboServiceLatchCommandLineRunner");
		runner.setDomain(shutdownLatchDomainName);
		return runner;
		
	}
	
	
	
	
}

