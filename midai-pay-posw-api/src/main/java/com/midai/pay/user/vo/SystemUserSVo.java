package com.midai.pay.user.vo;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.midai.pay.user.entity.SystemUser;

public class SystemUserSVo {

	@NotEmpty
	private SystemUser su;
	@NotBlank
	private String roleIds;
	private String roleName;
	
	private String orgNames; 
	
	public String getOrgNames() {
		return orgNames;
	}
	public void setOrgNames(String orgNames) {
		this.orgNames = orgNames;
	}
	public SystemUser getSu() {
		return su;
	}
	public void setSu(SystemUser su) {
		this.su = su;
	}
	public String getRoleIds() {
		return this.roleIds;
	}
	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	
}
