/**
 * Project Name:midai-pay-common
 * File Name:OhterPayAutoConfig.java
 * Package Name:com.midai.framework.config.otherpay
 * Date:2016年9月19日上午11:05:44
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.framework.config.otherpay;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.InitDestroyAnnotationBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.midai.framework.config.mybatis.MybatisProperties;

/**
 * ClassName:OhterPayAutoConfig <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月19日 上午11:05:44 <br/>
 * @author   屈志刚
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Configuration
@EnableConfigurationProperties(OtherPayProperties.class)
@ConditionalOnNotWebApplication
public class OtherPayAutoConfig {
	
	@Autowired
	private OtherPayProperties otherPayProperties;
	
/*	@PostConstruct
	public void init(){
		System.out.println("otherPayProperties.getAlipayuoservice()"+otherPayProperties.getAlipayUoService());
	}*/
	
	

}

