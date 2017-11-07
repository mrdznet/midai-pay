package com.midai.pay.frozen.vo;

import java.util.Date;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class TradeFrozenVo {
	
	@ApiModelProperty(value="主键")
	private Integer id;
	
	@ApiModelProperty(value="流水号")
	private String  hostTransssn;
	
	@ApiModelProperty(value="商户小票号")
	private String mercId;
	
	@ApiModelProperty(value="商户名称 ")
	private String mercName;
	
	@ApiModelProperty(value="交易金额")
	private double transAmt;
	
	@ApiModelProperty(value="交易时间")
	private Date transTime;
	
	@ApiModelProperty(value="交易卡号")
	private String transCardNo;
	
	@ApiModelProperty(value="冻结时间")
	private Date frozenTime;
	
	@ApiModelProperty(value="冻结原因")
	private String frozenReason;
	
	@ApiModelProperty(value="冻结操作人")
	private String frozenPerson;
	
	@ApiModelProperty(value="解结操作人")
	private String unfrozenPerson;
	
	@ApiModelProperty(value="解冻日期")
	private Date unfrozenTime;
	
	
	

}
