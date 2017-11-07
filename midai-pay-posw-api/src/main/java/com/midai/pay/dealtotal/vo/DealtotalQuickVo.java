package com.midai.pay.dealtotal.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class DealtotalQuickVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/* 交易流水 */
	@ApiModelProperty(value="交易流水号")
	private String seqId;
	
	/*机构号*/
	@ApiModelProperty(value="机构号")
	private String orgaId;
	
	/*交易金额*/
	@ApiModelProperty(value="交易金额")
	private Integer transAmt;
	
	/*交易手续费*/
	@ApiModelProperty(value="交易手续费")
	private Integer transFee;

	/* 结算卡号 */
	@ApiModelProperty(value="结算卡号")
	private String cardNo;

	/*结算人姓名 */
	@ApiModelProperty(value="结算人姓名")
	private String cardOwer;

	/* 结算人身份证号 */
	@ApiModelProperty(value="结算人身份证号")
	private String cardOwerId;

	/* 详情 */
	@ApiModelProperty(value="详情")
	private String note;

	/* 收款方手机号 */
	@ApiModelProperty(value="收款方手机号")
	private String inMobile;

	/* 交易通道方 */
	@ApiModelProperty(value="交易通道方 (1-微信, 2-支付宝, 3-银联扫码, 4-花呗, 5-京东白条)")
	private String transChannel;
	
	/* 上游通道 */
	@ApiModelProperty(value="上游通道 ")
	private String upChannel;

	/* 1-扫码, 2- */
	@ApiModelProperty(value="1-扫码")
	private Integer type;

	/* 下游通道回调地址*/
	@ApiModelProperty(value="下游通道回调地址")
	private String notifyUrl;
	
	/* 商户号 */
	@ApiModelProperty(value="商户号")
	private String mercNo;
	
	@ApiModelProperty(value="商户名称")
	private String mercName;
	
	@ApiModelProperty(value="代理商编号")
	private String agentNo;
	
	@ApiModelProperty(value="数据来源 0 下游通道 1 米付")
	private Integer source;
	
	@ApiModelProperty(value="商户扣率")
	private Double transRate;
	
	@ApiModelProperty(value="备份")
	private String remarks;
}
