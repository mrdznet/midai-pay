package com.midai.pay.mobile.utils;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = MassageProperties.MSG_PREFIX)
@Data
public class MassageProperties implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String MSG_PREFIX="msg";
	
	public String url;
}
