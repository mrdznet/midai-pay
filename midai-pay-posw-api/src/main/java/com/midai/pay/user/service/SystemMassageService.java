package com.midai.pay.user.service;

public interface SystemMassageService {
	
	/**
	 * 
	 * @param pageMap 	接收的page
	 * @param toRole 消息接受者(此角色下所有人)
	 */
	public void sendMsgByRole(String pageMap, String inscode);
}
