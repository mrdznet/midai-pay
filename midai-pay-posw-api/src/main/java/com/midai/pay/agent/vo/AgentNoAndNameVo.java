package com.midai.pay.agent.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class AgentNoAndNameVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6912690639191043542L;

	/**
	 * 代理商编号
	 */
	@ApiModelProperty(value="代理商编号")
	private String agentNo;
	
	/**
	 * 代理商名称
	 */
	@ApiModelProperty(value="代理商名称")
	private String name;
	
	/**
	 * 顶级代理商编号
	 */
	@ApiModelProperty(value="顶级代理商编号")
	private String topAgentId;
	
	/**
	 * 顶级代理商名称
	 */
	@ApiModelProperty(value="顶级代理商名称")
	private String topAgentName;
	
}
