package com.midai.pay.autopaymoney.vo;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class AutoPayMoneyVo {

	@ApiModelProperty("提现日期")
	private String tixianDatetime;
	
	
	@ApiModelProperty("提现流水号")
	private String tixiLogno;
	
	@ApiModelProperty("商户小票号")
	private String mercId;
	
	@ApiModelProperty("商户名称")
	private String mercName;
	
	@ApiModelProperty("结算金额")
	private Double payMoney;
	
	@ApiModelProperty("开户名")
	private String accountName;
	
	@ApiModelProperty("开户账号")
	private String accountNo;
	
	@ApiModelProperty("开户行")
	private String bankName;
	
	@ApiModelProperty("结算日期")
	private Date payTime;
	
	@ApiModelProperty(value="结算手续费")
	private Double tixianFeeamt;
	
	@ApiModelProperty("结算状态 0:待结算;1:结算中;3:结算成功;4:结算失败")
	private Integer payState;

	@ApiModelProperty("结算状态文字说明")
	
	private String payStatestr;
	
	@ApiModelProperty("备注")
	private String errorMsg;
	
	@ApiModelProperty("代付渠道")
	private String payChannel;
	
	@ApiModelProperty("通道返回码")
	private String channelCode;
	
	@ApiModelProperty("提现手续费")
	private String mchntSingleFee;
	
	@ApiModelProperty("代理商名称")
	private String agentName;
	@ApiModelProperty("代理商等级")
	private String agentLevel;
	
	@ApiModelProperty("支付方式")
	private String payType;
	
	@ApiModelProperty("交易状态")
	private String state;
}
