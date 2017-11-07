package com.midai.pay.autopaymoney.entity;

/**
 * 打款状态枚举类
 */
public enum PayChannelEnum {
	
	CHANNEL_JZ("500302", "钜真代付"),
	CHANNEL_CJ("G10002", "畅捷支付"),
	CNANNEL_MS("MISHUA", "米刷代付")
	;
	
	private String channel; 
	private String name;

	private PayChannelEnum() {
	}
	
	private PayChannelEnum(String channel, String agentName) {
		this.channel = channel;
		this.name = agentName;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
