package com.midai.pay.agent.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class AgentAllVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="代理商信息")
	private AgentVo agentVo;
	
	@ApiModelProperty(value="代理商相关图片")
	private List<AgentImgVo> imgVo;
	
	
}	
