package com.midai.pay.mobile.service;

public interface MobileUserService {

	/**
	 * 登陆
	 * @param content
	 * @return
	 */
	Object login(String content, String code);

	/**
	 * 注册
	 * @param content
	 * @return
	 */
	Object registerExecute(String content);

	/**
	 * 修改密码
	 * @param value
	 * @return
	 */
	Object modifyPassword(String value);

	Object registerHaiBeiExecuteOne(String value);

	Object registerHaiBeiExecuteTwo(String value);

	Object updatePhone(String value);
}
