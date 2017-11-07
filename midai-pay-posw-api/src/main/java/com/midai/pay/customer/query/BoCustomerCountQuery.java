package com.midai.pay.customer.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;

import com.midai.framework.query.PaginationQuery;

import lombok.Data;


@Data
@ApiModel
public class BoCustomerCountQuery extends PaginationQuery implements Serializable {

	@Id
	@ApiModelProperty(value="商户编号")
	private String mercNo;
	
	@ApiModelProperty(value="代理商ID")
	private String agentId;
	
	@ApiModelProperty(value="代理商name")
	private String agentName;
	
	@ApiModelProperty(value="商户name")
	private String mercName;
	
	@ApiModelProperty(value="小票号")
	private String mercId;
	
	@ApiModelProperty(value="电话号码")
	private String mobile;
	
	@ApiModelProperty(value="开户名")
	private String accountName;
     
	@ApiModelProperty(value="开户账号")
	private String accountNo;
	
	@ApiModelProperty(value="开户支行ID")
	private String branchbankId;
	
	@ApiModelProperty(value="状态 0:初审中 1:风控审 2:初审未通过 3:风控未通过 4:审核通过")
    private Integer state;
	
    private String inscode;
    
    private Date createTime;
	
    private Date updateTime;
    
    private String allAgents;
    
    private String agentNo;
    private String loginName;
	@ApiModelProperty("等级：0-所有代理商, 1-子代理商")
	private String level;
}
