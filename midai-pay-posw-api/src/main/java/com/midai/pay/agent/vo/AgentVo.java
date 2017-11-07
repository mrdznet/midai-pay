package com.midai.pay.agent.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class AgentVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1247488416597929900L;

	@ApiModelProperty(value="主键")
	private Integer id;
	
	// 基本信息
	@ApiModelProperty(value="代理商编号")
	private String agentNo;
	@ApiModelProperty(value="代理商名称")
	private String name;
	@ApiModelProperty(value="登录账号")
	private String shortName;
	@ApiModelProperty(value="父代理商编号")
	private String parentAgentId;
	@ApiModelProperty(value="父代理商名称")
	private String parentAgentName;

	//证件信息
	@ApiModelProperty(value="营业执照号")
	private String licenceNo;
	@ApiModelProperty(value="税务登记号")
	private String taxId;
	@ApiModelProperty(value="法人代表")
	private String corpRepr;
	@ApiModelProperty(value="法人身份证号")
	private String corpReprNo;
	
	//联系方式
	@ApiModelProperty(value="代理商联系人")
	private String agentContact;
	@ApiModelProperty(value="代理商电话")
	private String mobile;
	@ApiModelProperty(value="代理商邮箱")
	private String agentEmail;
	@ApiModelProperty(value="邮政编码")
	private String postalCode;
	@ApiModelProperty(value="省编码")
	private String provinceCode;
	@ApiModelProperty(value="市编码")
	private String cityCode;
	@ApiModelProperty(value="区编码")
	private String areaCode;
	@ApiModelProperty(value="地址")
	private String address;
	
	//结算信息
	@ApiModelProperty(value="开户行名称")
	private String bankName;	
	@ApiModelProperty(value="银行ID")	
	private String bankCode;	
	@ApiModelProperty(value="开户支行名称")
	private String bankBranchName;	
	@ApiModelProperty(value="分支银行code")
	private String bankBranchCode;	
	@ApiModelProperty(value="结算账户名称")
	private String accountName;	
	@ApiModelProperty(value="结算账户号码")
	private String accountCardNo;	
	@ApiModelProperty(value="结算方式 1 对公，2 对私， 3 虚拟")
	private Integer settlingMethod;		
	@ApiModelProperty(value="T+0提现限额")
	private Double withdrawCashT0;	
	
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
	@ApiModelProperty(value="无卡成本信息-借记卡手续费率")
	private Double nonCardDebitRate;	
	@ApiModelProperty(value="无卡成本信息-贷记卡手续费率")
	private Double nonCardCreditRate;
	
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
	
	//机具押金
	@ApiModelProperty(value="机具押金-刷卡机具押金")
	private Double terminalSwipeDeposit;
	@ApiModelProperty(value="机具押金-刷卡机具返还条件")
	private Double terminalSwipeDepositReturn;
	@ApiModelProperty(value="机具押金-传统pos机具押金")
	private Double terminalPosDeposit;
	@ApiModelProperty(value="机具押金-传统pos机具返还条件")
	private Double terminalPosDepositReturn;
	@ApiModelProperty(value="机具押金-传统pos月押金")
	private Double terminalPosDepositMonthly;
	
	@ApiModelProperty(value="修改人")
	private String updateUser;
	@ApiModelProperty(value="标识位 1一级  2 二级   3 三级  默认0")
	private int markId;
	
	private Date createTime;
	private String custCount;
	private String ids;
}
