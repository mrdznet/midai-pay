 package com.midai.pay.user.query;

import java.io.Serializable;

import com.midai.framework.query.PaginationQuery;

public class SystemCommNodeQuery extends PaginationQuery implements Serializable {
	
	 
	 	 	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pid;
	private String name;
	private Integer orgType;
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getOrgType() {
		return orgType;
	}
	public void setOrgType(Integer orgType) {
		this.orgType = orgType;
	}
 	
	
		
}