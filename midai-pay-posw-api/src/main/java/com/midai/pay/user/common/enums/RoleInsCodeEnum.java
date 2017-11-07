package com.midai.pay.user.common.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 代理商枚举类
 */
public enum RoleInsCodeEnum {
	
	INSCODE_HB("101003", "HB_AGENT"), 
	INSCODE_ZF("101004", "ZF_AGENT"),
	INSCODE_MF("101002", "MF_AGENT");
	
	private String insCode; 
	private String roleName;

	private RoleInsCodeEnum() {
	}
	
	/**
	 * @param insCode
	 * @param agentName
	 */
	private RoleInsCodeEnum(String insCode, String roleName) {
		this.insCode = insCode;
		this.roleName = roleName;
	}

	public String getInsCode() {
		return insCode;
	}

	public void setInsCode(String insCode) {
		this.insCode = insCode;
	}


	public String getRoleName() {
		return roleName;
	}


	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}


	public static String getStatusName(String inscode) {
		RoleInsCodeEnum[] enums = values();
		for (RoleInsCodeEnum e : enums) {
			if (e.getInsCode().equals(inscode)) {
				return e.getRoleName();
			}
		}
		return null;
	}
	
	public static String getKey(String roleName) {
		RoleInsCodeEnum[] enums = values();
		for (RoleInsCodeEnum e : enums) {
			if (e.toString().equalsIgnoreCase(roleName)) {
				return e.getInsCode();
			}
		}
		return null;
	}

	public static List<RoleInsCodeEnum> getStatusName() {
		List<RoleInsCodeEnum> list = new ArrayList<RoleInsCodeEnum>();
		RoleInsCodeEnum[] enums = values();
		for (RoleInsCodeEnum e : enums) {
			list.add(e);

		}
		return list;
	}

}
