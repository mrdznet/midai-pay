package com.midai.pay.agent.query;

import java.io.Serializable;

import com.midai.framework.query.PaginationQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 
*    
* 项目名称：midai-pay-posw-api   
* 类名称：AgentProfitQuery   
* 类描述：   分润查询条件类
* 创建人：wrt   
* 创建时间：2017年7月5日 上午11:09:47   
* 修改人：wrt   
* 修改时间：2017年7月5日 上午11:09:47   
* 修改备注：   
* @version    
*
 */


@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel("代理商分润查询条件类")
public class AgentProfitQuery  extends PaginationQuery implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("清算开始时间")
	private String settlementTimeStart;
	
	@ApiModelProperty("清算结束时间")
	private String settlementTimeEnd;
	
	@ApiModelProperty("代理商编号")
	private String agentId;
	
	@ApiModelProperty("代理商名称")
	private String agentName;
	
	@ApiModelProperty("商户等级， 0本级 1所有下级")
	private Integer flag;
	
	@ApiModelProperty("本级代理商规则编码")
	private String code;
	
	@ApiModelProperty("父代理商编号")
	private String parentAgentId;
	
	@ApiModelProperty("当前用户名称")
	private String userName;
	
	@ApiModelProperty("商户号")
	private String mercNo;

	@ApiModelProperty("当前用户类型: sys系统用户 agent 代理商")
	private String userFlag;
}
