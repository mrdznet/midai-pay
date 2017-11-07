package com.midai.pay.posp.config;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = PayMiShuaProperties.MISHUAI_PREFIX)
public class PayMiShuaProperties implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4717358953378875885L;

	public static final String MISHUAI_PREFIX="paymishua";
	
	private String host;
	
	private int port;
	
	private String instcode;
	
	private String mercid;
	
	private String deviceid;

}
