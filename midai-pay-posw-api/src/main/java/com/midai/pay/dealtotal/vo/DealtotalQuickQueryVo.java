package com.midai.pay.dealtotal.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class DealtotalQuickQueryVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="交易流水号")
	private String seqId;
	
	@ApiModelProperty(value="返回码 ")
	private String payResult;
	
	@ApiModelProperty(value="返回码信息 ")
	private String payDesc;
	
	@ApiModelProperty(value="返回信息")
	private String note;
	
	@ApiModelProperty(value="商户号")
	private String mercNo;
	
	@ApiModelProperty(value="商户名称")
	private String mercName;
	
	@ApiModelProperty(value="代理商编号")
	private String agentNo;
	
	@ApiModelProperty(value="代理商名称")
	private String agentName;
	
	@ApiModelProperty(value="交易通道方 (1-微信, 2-支付宝, 3-银联扫码, 4-花呗, 5-京东白条)")
	private String transChannel;
	
	private String transChannelCode;
	
	@ApiModelProperty(value="交易金额")
	private Double transAmt;
	
	@ApiModelProperty(value="交易时间")
	private Date createTime;
}
