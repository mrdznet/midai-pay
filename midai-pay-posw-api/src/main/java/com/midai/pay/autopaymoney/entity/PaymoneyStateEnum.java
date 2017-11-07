package com.midai.pay.autopaymoney.entity;

/**
 * 打款状态枚举类
 */
public enum PaymoneyStateEnum {
	
	PAY_STATE_WAITING(0, "待结算"),
	PAY_STATE_ING(1, "系统结算中"),
	PAY_STATE_SUCCESS(3, "结算成功"), 
	PAY_STATE_ERROR(4, "结算失败"),
	PAY_STATE_PROCESS(5, "渠道结算中"),
	PAY_STATE_UNKNOWN(6, "状态未明")
	;
	
	private Integer state; 
	private String name;

	private PaymoneyStateEnum() {
	}
	
	private PaymoneyStateEnum(Integer insCode, String agentName) {
		this.state = insCode;
		this.name = agentName;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
