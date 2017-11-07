package com.midai.pay.agent.query;

import java.io.Serializable;

import com.midai.framework.query.PaginationQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class AgentQuery extends PaginationQuery implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4031742385099597431L;

	@ApiModelProperty("代理商编号")
	private String agentNo;
	
	@ApiModelProperty("代理商名称")
	private String name;
	
	@ApiModelProperty("父代理商编号")
	private String parentAgentId;
	
	@ApiModelProperty("父代理商名称")
	private String parentAgentName;
	
	@ApiModelProperty("源代理商编号")
	private String agentNor;
	
	private String allAgents;
	
	private String mobile;
	
	private String loginName;
	
	@ApiModelProperty("代理商等级：0-所有代理商, 1-子代理商")
	private String level;

	private String aNo;
}
