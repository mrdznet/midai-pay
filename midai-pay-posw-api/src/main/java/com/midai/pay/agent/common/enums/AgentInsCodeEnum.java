package com.midai.pay.agent.common.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 代理商枚举类
 */
public enum AgentInsCodeEnum {
	
	INSCODE_HB("101003", "海贝代理商"), 
	INSCODE_ZF("101004", "掌付代理商"),
	INSCODE_MF("101002", "米付代理商");
	
	private String insCode; 
	private String agentName;

	private AgentInsCodeEnum() {
	}
	
	/**
	 * @param insCode
	 * @param agentName
	 */
	private AgentInsCodeEnum(String insCode, String agentName) {
		this.insCode = insCode;
		this.agentName = agentName;
	}

	public String getInsCode() {
		return insCode;
	}

	public void setInsCode(String insCode) {
		this.insCode = insCode;
	}


	public String getAgentName() {
		return agentName;
	}


	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}


	public static String getStatusName(String inscode) {
		AgentInsCodeEnum[] enums = values();
		for (AgentInsCodeEnum e : enums) {
			if (e.getInsCode() == inscode) {
				return e.getAgentName();
			}
		}
		return null;
	}
	
	public static String getKey(String agentName) {
		AgentInsCodeEnum[] enums = values();
		for (AgentInsCodeEnum e : enums) {
			if (e.toString().equalsIgnoreCase(agentName)) {
				return e.getInsCode();
			}
		}
		return null;
	}

	public static List<AgentInsCodeEnum> getStatusName() {
		List<AgentInsCodeEnum> list = new ArrayList<AgentInsCodeEnum>();
		AgentInsCodeEnum[] enums = values();
		for (AgentInsCodeEnum e : enums) {
			list.add(e);

		}
		return list;
	}

}
