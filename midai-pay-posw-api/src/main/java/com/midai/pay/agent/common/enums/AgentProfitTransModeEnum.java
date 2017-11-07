package com.midai.pay.agent.common.enums;

public enum AgentProfitTransModeEnum {

	
	TRANSTYPE_MPOS_JJK("1001", "借记卡"), 
	TRANSTYPE_MPOS_DJK("1002", "贷记卡"), 
	TRANSTYPE_NO_CARD("2001", "无卡"), 
	TRANSTYPE_POS_JJK("3001", "借记卡"), 
	TRANSTYPE_POS_DJK("3002", "贷记卡"), 
	TRANSTYPE_SCAN_WIN("4001", "微信"), 
	TRANSTYPE_SCAN_ZFB("4002", "支付宝"), 
	TRANSTYPE_SCAN_YL("4003", "银联扫码"), 
	TRANSTYPE_SCAN_HB("4004", "花呗"),
	 TRANSTYPE_SCAN_JDBT("4005", "京东白条");
	
	private String code; 
	private String name;

	private AgentProfitTransModeEnum() {
	}
	
	private AgentProfitTransModeEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public static String getName(Integer code) {
		AgentProfitTransModeEnum[] enums = values();
		for (AgentProfitTransModeEnum e : enums) {
			if (e.getCode().equals(code)) {
				return e.getName();
			}
		}
		return null;
	}
	
	public static String getKey(String name) {
		AgentProfitTransModeEnum[] enums = values();
		for (AgentProfitTransModeEnum e : enums) {
			if (e.getName().equals(name)) {
				return e.getCode();
			}
		}
		return null;
	}

}
