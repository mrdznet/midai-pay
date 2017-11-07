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

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="tbl_bo_agent_templet")
public class AgentTemplet extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = -17180146147442988L;

	@Id
	private Integer id;
	
	@Column(name="agent_no", length=100, unique=true)
	@NotBlank
	private String agentNo;
	
	// 模板类型（1-商户模板, 2-代理商模板）
	@Column(name="type")
	private Integer type;
	
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
	//扫码成本信息-蚂蚁花呗手续费率
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
	
	
	// 分润比例- m-pos分润比例
	@Column(name="profit_mpos", precision=10, scale=2)
	private Double profitMpos;
	// 分润比例- 无卡分润比例
	@Column(name="profit_no_card", precision=10, scale=2)
	private Double profitNoCard;
	// 分润比例- 大pos分润比例
	@Column(name="profit_terminal_pos", precision=10, scale=2)
	private Double profitTerminalPos;
	// 分润比例- 扫码分润比例
	@Column(name="profit_scan_code", precision=10, scale=2)
	private Double profitScanCode;
	
}
