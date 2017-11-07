package com.midai.pay.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = OSSProperties.OSS_PROPERTIES_PREFIX)
public class OSSProperties {
	
	public static final String OSS_PROPERTIES_PREFIX = "mifu.oss";
	
	private String uploadEndpoint;
	private String endpoint;
	private String accessid;
	private String accesskey;
	private String bucket;
	private String rootDir;
	private String expireTime;
	private String callbackUrl;
	
	private String ecsTempDir;
	private String ecsZipDir;
	private String zipDir;
	private String endpointEcs;
	private String ignoreZipDir;
	private String img;
	private String host;
}
