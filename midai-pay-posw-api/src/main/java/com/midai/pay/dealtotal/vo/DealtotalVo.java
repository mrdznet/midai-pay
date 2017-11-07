package com.midai.pay.dealtotal.vo;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ClassName:User <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月21日  <br/>
 * @author   zt
 * @version  
 * @since    JDK 1.7
 * @see 	 
*/
@Data
@ApiModel
public class DealtotalVo {
	
	@ApiModelProperty(value="交易流水号")
	private String hostTransSsn;

	@ApiModelProperty(value="返回码")
	private String respCdLoc;
	
	@ApiModelProperty(value="返回信息")
	private  String respCdLocDsp;

	@ApiModelProperty(value="商户小票号")
    private String mchntCodeIn;

	@ApiModelProperty(value="商户名称")
	private String mchntName;
	
	 @ApiModelProperty(value="代理商名称")
	 private String agentName;
	
	 @ApiModelProperty(value="交易方式(1-mpos, 2-无卡, 3-大pos)")
	 private String payMode;
	
	 @ApiModelProperty(value="交易时间")
	 private Date transTime;
	 
	/* @ApiModelProperty(value="交易总数")
	 private Integer cc;*/
	 
	 @ApiModelProperty(value="交易金额")
	 private String transAmt;
	 
	 @ApiModelProperty(value="银行卡号")
	 private String transCardNo;
	 
	 @ApiModelProperty(value="卡类型")
	 private  String cardKind;
	 
	 @ApiModelProperty(value="交易类型(消费-0200,消费冲正-0400,余额查询-0210,提现申请-0500,转账-0700,信用卡还款-0600,退款-0220) ")
	 private String transCode;
	 
	 
	 
	 @ApiModelProperty(value="商户手机号")
	 private String mobile;
	
	@ApiModelProperty(value="机身号")
	private String deviceNoIn;	
	
	@ApiModelProperty(value="发卡行")
	private  String cardIssName;
	
	@ApiModelProperty(value="发卡行编号")
	private String cardIssId;
	
	@ApiModelProperty(value="商户类型")
	private String specialFeeType;
	
	@ApiModelProperty(value="交易状态")
	private String transStatus;
	 
	 @ApiModelProperty(value="大商户")
	 private String mchntCodeOut;
	 
	 @ApiModelProperty(value="通道机构")
	 private   String routInstIdCd;
	 
	 @ApiModelProperty(value="代理商编号")
	 private String agentId;
	
	 @ApiModelProperty(value="通道借记卡结算手续费（固定值）%")
	 private Double instFeeRate;
	
	 @ApiModelProperty(value="通道贷记卡结算手续费（固定值）%")
	 private Double instDfeeRate;
	 
	 @ApiModelProperty(value="结算手续费（固定值）")
	 private Double instaFeeRate;
	 
	 @ApiModelProperty(value="借记卡结算手续费（封顶值）")
	 private Double instFeeMax;
	 
	 @ApiModelProperty(value="商户扣率") 
	 private Double mchntRate;
	 
	 @ApiModelProperty(value="商户单笔扣率") 
	 private Double mchntSingleFee;
	
	 @ApiModelProperty(value="结算金额")
	 private String tixianAmt;
	 
	 @ApiModelProperty(value="提现手续费")
	 private String tixianFeeamt;
	 
	 @ApiModelProperty(value="通道返回码")
	 private String channelCode;
	
	 @ApiModelProperty(value="打款流水号")
	 private String tixiLogno;

	 @ApiModelProperty(value="卡类型")
	 private String cardKindState;
	 
	 @ApiModelProperty(value="通道手续费")
	 private String transFee;
	 
	 @ApiModelProperty(value="通道结算金额")
	 private String channelMoney;
	 
	 @ApiModelProperty(value="利润")
	 private String profits;
	 
	 @ApiModelProperty(value="顶级代理商")
	 private String inscode;
	 
	 @ApiModelProperty(value="商户编号")
	 private String mercNo;
	 
}
