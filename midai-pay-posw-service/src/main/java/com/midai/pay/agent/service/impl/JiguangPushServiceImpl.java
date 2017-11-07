package com.midai.pay.agent.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.agent.service.JiguangPushService;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

@Service
public class JiguangPushServiceImpl implements JiguangPushService{
	
	private Logger logger = LoggerFactory.getLogger(JiguangPushServiceImpl.class);
	
	@Value("${jpush.appkey}")
	private String appKey;
	
	@Value("${jpush.mastersecret}")
	private String masterSecret;

	@Override
	public PushResult push(String mobile,String msg) {
		PushPayload payload = createPushPayLoad(mobile,msg);
		JPushClient jpushClient = new JPushClient(masterSecret, appKey, null,
				ClientConfig.getInstance());
		try {
			PushResult result = jpushClient.sendPush(payload);
			return result;
		} catch (APIConnectionException e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		} catch (APIRequestException e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		}
		return null;
	}

	@Override
	public PushPayload createPushPayLoad(String alias,String msg) {
		return PushPayload.newBuilder()
				.setPlatform(Platform.all())
				.setAudience(Audience.alias(alias))
				.setNotification(Notification.newBuilder()
						.setAlert(msg)
						.addPlatformNotification(IosNotification.newBuilder()
								.autoBadge()
								.build())
						.build())
				.setMessage(Message.newBuilder()
						.setMsgContent(msg)
						.build())
				.build();
	}

}
