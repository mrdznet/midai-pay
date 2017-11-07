package com.midai.pay.agent.service;


import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;


public interface JiguangPushService{
	
	/**
	 * 推送通知
	 * @param msg
	 */
	public PushResult push(String mobile,String msg);
	
	public PushPayload createPushPayLoad(String alias,String msg);
}
