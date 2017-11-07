package com.midai.pay.handpay.service;

import java.util.Map;

import com.midai.pay.handpay.vo.AuthBean;
import com.midai.pay.handpay.vo.CustomerAuthBean;

public interface JiDian4AuthService {
	
	// 四要素验证
	Map<String, String>  auth4(AuthBean auth);
	
	//认证查询
	Map<String, String> authQuery(String orderId);
	
	Object mobileAuth4(AuthBean auth);
	
	Object ifAlreadyAuth(CustomerAuthBean authInfo);
}
