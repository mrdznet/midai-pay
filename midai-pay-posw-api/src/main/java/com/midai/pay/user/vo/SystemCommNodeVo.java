package com.midai.pay.user.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SystemCommNodeVo {

	private String id;
	private String pId;
	private String name;
	/**
	 * 类型 1公司 2部门 3代理商  4用户  5角色
	 */
	private Integer orgType;
	private Integer level;
	private Boolean open;
}
