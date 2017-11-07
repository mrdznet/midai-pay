package com.midai.pay.user.vo;

import com.midai.pay.user.entity.SystemRole;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SystemRoleSVo {

	private SystemRole sr;
	private Integer orgid;
	private String specIds;
	private String smvIds;
	
}
