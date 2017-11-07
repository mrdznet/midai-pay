/**
 * Project Name:midai-pay-posp-service
 * File Name:MidaiPayHsmProperties.java
 * Package Name:com.midai.pay.posp.config
 * Date:2016年11月16日上午9:26:33
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.posp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * ClassName:MidaiPayHsmProperties <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年11月16日 上午9:26:33 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Data
@ConfigurationProperties(prefix = MidaiPayHsmProperties.HSM_PREFIX)
public class MidaiPayHsmProperties {
	
	public static final String HSM_PREFIX="hsm";
	
	private String main;
	
	private String back;
	
	private int port;
	
	private int timeout;

}

