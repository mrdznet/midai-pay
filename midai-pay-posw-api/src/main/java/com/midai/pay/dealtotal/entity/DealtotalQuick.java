package com.midai.pay.dealtotal.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ClassName:User <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年9月21日 <br/>
 * 
 * @author zt
 * @version
 * @since JDK 1.7
 * @see
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "tbl_deal_total_quick")
public class DealtotalQuick extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	
	/* 交易流水 */
	@Column(name = "seq_id", length = 12, unique = true)
	@NotNull
	private String seqId;

	/*机构号*/
	@Column(name = "orga_id")
	private String orgaId;
	
	/*交易金额*/
	@Column(name = "trans_amt")
	private Integer transAmt;
	
	/*交易手续费*/
	@Column(name = "trans_fee")
	private Integer transFee;
	
	/*交易手续费率*/
	@Column(name = "trans_rate")
	private Double transRate;

	/* 结算卡号 */
	@Column(name = "card_no")
	private String cardNo;

	/*结算人姓名 */
	@Column(name = "card_ower")
	private String cardOwer;

	/* 结算人身份证号 */
	@Column(name = "card_ower_id")
	private String cardOwerId;

	/* 详情 */
	@Column(name = "note")
	private String note;

	/* 收款方手机号 */
	@Column(name = "in_mobile")
	private String inMobile;

	/* 交易通道方 */
	@Column(name = "trans_channel")
	private String transChannel;
	
	/* 上游通道 */
	@Column(name = "up_channel")
	private String upChannel;

	/* 1-扫码, 2- */
	@Column(name = "type")
	private Integer type;

	/* 支付状态 */
	@Column(name = "pay_desc")
	private String payDesc;
	
	/* 支付状态码 */
	@Column(name = "pay_result")
	private String payResult;
	
	/* 代付状态 码 */
	@Column(name = "t0_resp_code")
	private String t0RespCode;
	
	/* 代付状态   */
	@Column(name = "t0_resp_desc")
	private String t0RespDesc;

	/* 通知地址   */
	@Column(name = "notify_url")
	private String notifyUrl;
	
	/* 商户号   */
	@Column(name = "merc_no")
	private String mercNo;
	
	/* 商户名称   */
	@Column(name = "merc_name")
	private String mercName;
	
	/* 代理商   */
	@Column(name = "agent_no")
	private String agentNo;
	
	@Column(name = "source")
	private Integer source;
	
	@Column(name = "remarks")
	private String remarks;
	
	private Date createTime;
	private Date updateTime;

}
