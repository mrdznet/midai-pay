 package com.midai.pay.user.query;

import java.io.Serializable;

import com.midai.framework.query.QueryBase;

public class SystemRoleQuery extends QueryBase implements Serializable {
	
	 
	 	 	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String rolename;
 	private String rolecode;
 	
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public String getRolecode() {
		return rolecode;
	}
	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}
	 
		
}