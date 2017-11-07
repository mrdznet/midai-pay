package com.midai.pay.agent.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 代理商
 * @author miaolei
 * 2016年9月20日 下午6:18:09
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="tbl_bo_agent")
public class Agent extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = -17180146147442988L;

	/**
	 * 主键
	 */
	@Id
	private Integer id;
	/**
	 * 代理商编号
	 */
	@Column(name="agent_no", length=100, unique=true)
	@NotBlank
	private String agentNo;
	/**
	 * 代理商名称
	 */
	@Column(name="name", length=200, unique=true)
	@NotBlank
	private String name;
	/**
	 * 代理商简称
	 */
	@Column(name="short_name", length=50, unique=true)
	@NotBlank
	private String shortName;
	/**
	 * 代理商编号
	 */
	@Column(name="org_code", length=100, unique=true)
	@NotBlank
	private String orgCode;
	/**
	 * 父代理商编号
	 */
	@Column(name="parent_agent_id", length=30)
	@NotBlank
	private String parentAgentId;
	/**
	 * 父代理商名称
	 */
	@Column(name="parent_agent_name", length=100)
	private String parentAgentName;
	
	
	
	// 营业执照号
	@Column(name="licence_no", length=100)
	private String licenceNo;
	//税务登记号
	@Column(name="tax_id", length=100)
	private String taxId;
	//法人代表
	@Column(name="corp_repr", length=100)
	private String corpRepr;
	//法人身份证号
	@Column(name="corp_repr_no", length=100)
	private String corpReprNo;
	
	
	
	//代理商联系人
	@Column(name="agent_contact", length=100)
	private String agentContact;
	// 代理商电话
	@Column(name="mobile", length=20, unique=true)
	@NotBlank
	private String mobile;
	//代理商邮箱
	@Column(name="agent_email", length=100)
	private String agentEmail;
	//邮政编码
	@Column(name="postal_code", length=100)
	private String postalCode;
	//省编码
	@Column(name="province_code", length=100)
	private String provinceCode;
	//市编码
	@Column(name="city_code", length=100)
	private String cityCode;
	//区编码
	@Column(name="area_code", length=100)
	private String areaCode;
	// 地址
	@Column(name="address", length=200)
	private String address;
	
	
	// 开户行名称
	@Column(name="bank_name", length=100)
	private String bankName;
	//银行ID
	@Column(name="bank_code", length=100)
	private String bankCode;	
	//开户支行名称
	@Column(name="bank_branch_name", length=100)
	private String bankBranchName;
	//分支银行code
	@Column(name="bank_branch_code", length=100)
	private String bankBranchCode;
	//结算账户名称
	@Column(name="account_name", length=100)
	private String accountName;
	//结算账户号码
	@Column(name="account_card_no", length=100)
	private String accountCardNo;
	// 结算方式 1 对公，2 对私， 3 虚拟
	@Column(name="settling_method", length=2)
	private Integer settlingMethod;
	//T+0提现限额
	@Column(name="withdraw_cash_t0", precision=10, scale=2)
	private Double withdrawCashT0;
	
	
	
	//刷卡成本信息-借记卡封顶手续费
	@Column(name="swing_card_limit", precision=10, scale=2)
	private Double swingCardLimit;	
	//刷卡成本信息-借记卡手续费
	@Column(name="swing_card_debit_rate", precision=10, scale=2)
	private Double swingCardDebitRate;
	//刷卡成本信息-贷记卡手续费
	@Column(name="swing_card_credit_rate", precision=10, scale=2)
	private Double swingCardCreditRate;	
	//刷卡成本信息-交易结算手续费
	@Column(name="swing_card_settle_fee", precision=10, scale=2)
	private Double swingCardSettleFee;	
	
	//无卡成本信息-借记卡手续费率
	@Column(name="non_card_debit_rate", precision=10, scale=2)
	private Double nonCardDebitRate;	
	//无卡成本信息-贷记卡手续费率
	@Column(name="non_card_credit_rate", precision=10, scale=2)
	private Double nonCardCreditRate;
	
	//传统POS成本信息-借记卡封顶手续费
	@Column(name="pos_debit_limit", precision=10, scale=2)
	private Double posDebitLimit;
	//传统POS成本信息-借记卡手续费
	@Column(name="pos_debit_rate", precision=10, scale=2)
	private Double posDebitRate;
	//传统POS成本信息-贷记卡手续费
	@Column(name="pos_credit_rate", precision=10, scale=2)
	private Double posCreditRate;	
	//传统POS成本信息-交易结算手续费
	@Column(name="pos_settle_fee", precision=10, scale=2)
	private Double posSettleFee;	
	
	//扫码成本信息-微信手续费率
	@Column(name="scan_code_wx_rate", precision=10, scale=2)
	private Double scanCodeWxRate;
	//扫码成本信息-支付宝手续费率
	@Column(name="scan_code_zfb_rate", precision=10, scale=2)
	private Double scanCodeZfbRate;
	//扫码成本信息-银联扫码手续费率
	@Column(name="scan_code_yl_rate", precision=10, scale=2)
	private Double scanCodeYlRate;
	//扫码成本信息-京东白条手续费
	@Column(name="scan_code_jdbt_rate", precision=10, scale=2)
	private Double scanCodeJdbtRate;
	//扫码成本信息-其它手续费
	@Column(name="scan_code_other_rate", precision=10, scale=2)
	private Double scanCodeOtherRate;
	//扫码成本信息-支付宝手续费率
	@Column(name="scan_code_myhb_rate", precision=10, scale=2)
	private Double scanCodeMyhbRate;
	
	
	//机具押金-刷卡机具押金
	@Column(name="terminal_swipe_deposit", precision=10, scale=2)
	private Double terminalSwipeDeposit;
	//机具押金-刷卡机具返还费
	@Column(name="terminal_swipe_deposit_return", precision=10, scale=2)
	private Double terminalSwipeDepositReturn;
	//机具押金-传统pos机具押金
	@Column(name="terminal_pos_deposit", precision=10, scale=2)
	private Double terminalPosDeposit;
	//机具押金-传统pos机具返还费
	@Column(name="terminal_pos_deposit_return", precision=10, scale=2)
	private Double terminalPosDepositReturn;
	//机具押金-传统pos月押金
	@Column(name="terminal_pos_deposit_monthly", precision=10, scale=2)
	private Double terminalPosDepositMonthly;
	
	
	
	@Column(name="code")
	private String code;
	
	@Column(name="create_user")
	private String createUser; // 创建人
	
	@Column(name="update_user")
	private String updateUser;	// 修改人
	
	@Column(name="op_state", length=10)
	private String opState;	// 状态 0:关闭,1:开通
	
	
	
//	--------------------------------------------------------------------------------------------------------
	
	/**
	 * 成本扣率
	 */
	@Column(name="cost_rate", precision=10, scale=2)
	private Double costRate;
	/**
	 * 商户扣率
	 */
	@Column(name="merchant_rate", precision=10, scale=2)
	private Double merchantRate;
	/**
	 * 代理商等级
	 */
	@Column(name="agent_levle")
	private Integer agentLevle;
	/**
	 * 备注
	 */
	@Column(name="apply_rate", length=250)
	private String applyRate;
	/**
	 * 单笔手续费
	 */
	@Column(name="single_money" ,precision= 8,scale=2)
	private Double singleMoney;
	
	@Column(name="inscode")
	private String inscode;
	
	@Column(name="cust_count")
	private String custCount;
}
