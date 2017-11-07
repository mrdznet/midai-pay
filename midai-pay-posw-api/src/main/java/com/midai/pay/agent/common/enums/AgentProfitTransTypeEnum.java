package com.midai.pay.agent.common.enums;

public enum AgentProfitTransTypeEnum {

	TRANSTYPE_MPOS(1, "MPOS"), 
	TRANSTYPE_NONCARD(2, "无卡"), 
	TRANSTYPE_POS(3, "传统POS"), 
	TRANSTYPE_SCANWX(4, "扫码-微信"), 
	TRANSTYPE_SCANZFB(5, "扫码-支付宝"), 
	TRANSTYPE_SCANYL(6, "扫码-银联"), 
	TRANSTYPE_SCANMYHB(7, "扫码-花呗"), 
	TRANSTYPE_SCANOTHER(8, "扫码-其他"), 
	TRANSTYPE_SCANJDBT(9, "扫码-京东");
	
	private Integer code; 
	private String name;

	private AgentProfitTransTypeEnum() {
	}
	
	private AgentProfitTransTypeEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}



	public void setCode(Integer code) {
		this.code = code;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public static String getName(Integer code) {
		AgentProfitTransTypeEnum[] enums = values();
		for (AgentProfitTransTypeEnum e : enums) {
			if (e.getCode().equals(code)) {
				return e.getName();
			}
		}
		return null;
	}
	
	public static Integer getKey(String name) {
		AgentProfitTransTypeEnum[] enums = values();
		for (AgentProfitTransTypeEnum e : enums) {
			if (e.getName().equals(name)) {
				return e.getCode();
			}
		}
		return null;
	}

}
