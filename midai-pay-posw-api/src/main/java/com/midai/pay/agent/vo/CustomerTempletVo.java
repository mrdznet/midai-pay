package com.midai.pay.agent.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class CustomerTempletVo implements Serializable {
	
	private static final long serialVersionUID = 1247488416597929900L;

	// 基本信息
	@ApiModelProperty(value="代理商编号")
	private String agentNo;
	@ApiModelProperty(value="代理商名称")
	private String name;
	//刷卡成本信息
	@ApiModelProperty(value="刷卡成本信息-借记卡封顶手续费")
	private Double swingCardLimit;	
	@ApiModelProperty(value="刷卡成本信息-借记卡手续费")
	private Double swingCardDebitRate;
	@ApiModelProperty(value="刷卡成本信息-贷记卡手续费")
	private Double swingCardCreditRate;	
	@ApiModelProperty(value="刷卡成本信息-交易结算手续费")
	private Double swingCardSettleFee;
	
	
	//无卡成本信息
	@ApiModelProperty(value="无卡成本信息")
	private Double nonCardRate;
	
	//传统POS成本信息
	@ApiModelProperty(value="传统POS成本信息-借记卡封顶手续费")
	private Double posDebitLimit;
	@ApiModelProperty(value="传统POS成本信息-借记卡手续费")
	private Double posDebitRate;	
	@ApiModelProperty(value="传统POS成本信息-贷记卡手续费")
	private Double posCreditRate;	
	@ApiModelProperty(value="传统POS成本信息-交易结算手续费")
	private Double posSettleFee;	
	
	
	//扫码成本信息
	@ApiModelProperty(value="扫码成本信息-微信手续费率")
	private Double scanCodeWxRate;	
	@ApiModelProperty(value="扫码成本信息-支付宝手续费率")
	private Double scanCodeZfbRate;
	@ApiModelProperty(value="扫码成本信息-银联扫码手续费率")
	private Double scanCodeYlRate;
	@ApiModelProperty(value="扫码成本信息-京东白条手续费")
	private Double scanCodeJdbtRate;
	@ApiModelProperty(value="扫码成本信息-其它手续费")
	private Double scanCodeOtherRate;
	@ApiModelProperty(value="扫码成本信息-蚂蚁花呗")
	private Double scanCodeMyhbRate;
	
	// 分润比例
	@ApiModelProperty(value="分润比例- m-pos分润比例")
	private Double profitMpos;
	@ApiModelProperty(value="分润比例- 无卡分润比例")
	private Double profitNoCard;
	@ApiModelProperty(value="分润比例- 大pos分润比例")
	private Double profitTerminalPos;
	@ApiModelProperty(value="分润比例- 扫码分润比例")
	private Double profitScanCode;
}
