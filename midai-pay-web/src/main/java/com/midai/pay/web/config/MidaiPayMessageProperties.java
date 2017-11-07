package com.midai.pay.web.config;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = MidaiPayMessageProperties.PREFIX)
public class MidaiPayMessageProperties implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String PREFIX="midai.pay.message";
	
	private String host;
	private int port;
	private String application;
	
	

}
