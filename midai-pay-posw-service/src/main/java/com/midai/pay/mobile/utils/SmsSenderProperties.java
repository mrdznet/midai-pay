package com.midai.pay.mobile.utils;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = SmsSenderProperties.SMS_PREFIX)
@Data
public class SmsSenderProperties implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String SMS_PREFIX="mifu.sms";
	
    public String url;// 请求地址
    public String port;// 请求端口
    public String accountSid;// 主账号
    public String accountToken; // 主账号token
    public String appId; // 应用ID
    public String callCenter;// 客服中心号
}
