/**
 * Project Name:midai-pay-common
 * File Name:OtherPayProperties.java
 * Package Name:com.midai.framework.config.otherpay
 * Date:2016年9月19日上午11:01:34
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.framework.config.otherpay;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;



/**
 * ClassName:OtherPayProperties <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月19日 上午11:01:34 <br/>
 * @author   屈志刚
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Data
@ConfigurationProperties(prefix = OtherPayProperties.OHTER_PAY_PREFIX)
public class OtherPayProperties {
	
	public static final String OHTER_PAY_PREFIX="other.pay";
	
	/**
	 * 接口类型
	 */
	private String alipayUoService;
	
	private String alipayUoMchId;
	
	
	
	
	
	
	
	
	
	
	
	
}

