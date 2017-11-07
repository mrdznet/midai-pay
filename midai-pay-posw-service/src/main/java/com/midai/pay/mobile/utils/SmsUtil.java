package com.midai.pay.mobile.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jianzhou.sdk.BusinessService;

public class SmsUtil {

	private  Logger logger = LoggerFactory.getLogger(SmsUtil.class);
	
	private final SmsProperties smsProperties;
	
	private final BusinessService bs;

	public SmsUtil (SmsProperties smsProperties,BusinessService bs){
		this.smsProperties=smsProperties;
		this.bs=bs;
	}


	public int sendSMS(String phonenumber, String content, int type, String inscode) {
		
		int sendMessCount = 0;
		bs.setWebService(smsProperties.getUrl());
		String account1 = "";
		String password1 = "";
		String boottitle1 = "";
		String td = "【安心智付】";
		if (null == inscode || inscode.isEmpty()) {
			inscode = "101";
		}
		if (inscode.equals("101") || inscode.equals("101002")) {
			account1 = smsProperties.getAccount();
			password1 = smsProperties.getPassword();
			boottitle1 = smsProperties.getBoottitle();
			content = content.replace("CALLCENTER", smsProperties.getCALLCENTER());
		}
		if (inscode.equals("101004")) {
			account1 = smsProperties.getAccountzf();
			password1 = smsProperties.getPasswordzf();
			boottitle1 = smsProperties.getBoottitlezf();
			content = content.replace("CALLCENTER", smsProperties.getCALLCENTERZF());
			td = "【掌上支付】";
		}
		logger.info(account1 + phonenumber);
		if (smsProperties.isSwitch()) {
			sendMessCount = bs.sendBatchMessage(account1, password1, phonenumber, content + "" + boottitle1);
		} else {
			sendMessCount = -1;
		}
		if (sendMessCount < 0) {
			logger.error(
					"#################### " + td + "# 手机号：" + phonenumber + "# 返回值：" + sendMessCount + "# 发送短信失败！");
		} else {
			logger.error(
					"####################" + td + "# 手机号：" + phonenumber + "# 返回值：" + sendMessCount + "# 发送短信成功！");
		}

		return sendMessCount;
	}

}
