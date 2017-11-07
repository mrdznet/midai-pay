package com.midai.pay.agent.common.enums;

public enum AgentProfitCustomerGradeEnum {

	GRADE_BJ(0, "本级"), 
	GRADE_XJ(1, "所有下级");
	
	private Integer code; 
	private String name;

	private AgentProfitCustomerGradeEnum() {
	}
	
	private AgentProfitCustomerGradeEnum(Integer code, String name) {
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
		AgentProfitCustomerGradeEnum[] enums = values();
		for (AgentProfitCustomerGradeEnum e : enums) {
			if (e.getCode().equals(code)) {
				return e.getName();
			}
		}
		return null;
	}
	
	public static Integer getKey(String name) {
		AgentProfitCustomerGradeEnum[] enums = values();
		for (AgentProfitCustomerGradeEnum e : enums) {
			if (e.getName().equals(name)) {
				return e.getCode();
			}
		}
		return null;
	}

}
