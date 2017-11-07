package com.midai.pay.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = OSSProperties.OSS_PROPERTIES_PREFIX)
public class OSSFolderProperties {
	
	public static final String OSS_PROPERTIES_PREFIX = "oss.folder";
	
	private String customerComm;
}
