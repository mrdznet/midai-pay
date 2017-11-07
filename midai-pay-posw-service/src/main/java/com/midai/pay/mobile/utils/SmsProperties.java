package com.midai.pay.mobile.utils;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = SmsProperties.SMS_PREFIX)
@Data
public class SmsProperties implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 756304992083154366L;
	
	public static final String SMS_PREFIX="mobile";
	
	public String url ;
	private String account ;
	private String password ;
	private String boottitle;
	public String CALLCENTER ;
	private String accountzf ;
	private String passwordzf ;
	private String boottitlezf ;
	public String CALLCENTERZF ;
	private boolean Switch ;

}
