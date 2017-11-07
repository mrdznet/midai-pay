package com.midai.pay.agent.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AgentProfitGradeVo {

	@ApiModelProperty("代理商编号")
	private String agentId;
	
	@ApiModelProperty("代理商规范代码")
	private String code;
	
	@ApiModelProperty("结算时间")
	private Date settlementTime;
}
