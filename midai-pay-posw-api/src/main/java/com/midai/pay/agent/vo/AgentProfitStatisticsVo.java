package com.midai.pay.agent.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("代理商分润统计查询结果")
@Data
public class AgentProfitStatisticsVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty("交易总笔数")
	private Integer transCount;
	
	@ApiModelProperty("总利润")
	private Double profitTotalAmount;
	
	@ApiModelProperty("总净利润")
	private Double profitRetainedAmount;
	
}
