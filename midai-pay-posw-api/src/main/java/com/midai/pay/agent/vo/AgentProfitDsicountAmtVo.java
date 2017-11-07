package com.midai.pay.agent.vo;

import lombok.Data;

@Data
public class AgentProfitDsicountAmtVo {

	//代理商信息
	private Double agentRate;
	private Double agentSingleFee;
	private Double agentLimitAmt;
	private Boolean agentIsLimit;
	
	//商户信息
	private Integer customerSingleFee;
	private Integer customerTransAmt;
	private Double customerRate;
	private String customerAgentId;
}
