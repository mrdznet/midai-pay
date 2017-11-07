package com.midai.pay.route.service;

import com.midai.pay.user.entity.User;

public interface RouteService {
	
	
	/** 
	 * @param moblie 内部商户手机号
	 * @param money 交易金额
	 * @return 外部大商户号
	 */
	 String route(String moblie,double money);
	 
	 String testActivity(User user);
}
