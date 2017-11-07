/**
 * Project Name:chenxun-framework-start
 * File Name:User.java
 * Package Name:com.chenxun.framework.entity
 * Date:2016年9月1日上午10:12:33
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.customer.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.midai.framework.common.BaseEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ClassName:User <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月21日  <br/>
 * @author   cjy
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="tbl_bo_customer")
public class BoCustomer extends BaseEntity implements Serializable{
    
	private static final long serialVersionUID = 1L;
    
	@Column(name="mer_type")
	private Integer merType;  // 商户类型  (0:MPOS, 1:微信支付宝)
	@Column(name="mer_auto")
	private Integer merAuto; // 商户类别  (0:MPOS商户, 1:普通商户)
	@Column(name="state")	// 状态  (0:申请中 |1:初审中|2:复审中|3:初审未通过|4:审核通过)
    private Integer state;
	@Column(name="opstate")
    private Integer opstate ;	// 商户状态 (0:关闭 1:开通 2:冻结 3:黑名单)
	@Column(name="channel")
	private Integer channel;	//商户渠道 (0:无卡, 1:大pos, 2:M-pos, 3:智能pos)
    @Column(name="customer_level")
    private String  customerLevel;	//商户等级(S,A,B,C)
	
	//  商户基本信息
	@Id
	@Column(name="merc_no")
	private String mercNo;	//商户编号
	@Column(name="agent_id")
	private String agentId;	//代理商ID
	@Column(name="agent_name")
	private String agentName;	//代理商name
	@Column(name="merc_name")
	private String mercName;	//商户名称
	@Column(name="merc_id")
	private String mercId; //小票号
	@Column(name="merc_name_short")
	private String mercNameShort;//商户简称
	
	// 商户附加信息
	@Column(name="industry")
	private String industry;	//商户所属行业
	@Column(name="mcc")
	private String mcc;	//商户类别(MCC)
	@Column(name="address")
	private String address;	//商户所属地址
    @Column(name="business_license")
    private String  businessLicense; //营业执照号
	@Column(name="legal_name")
	private String legalName; // 法人姓名
	@Column(name="mobile")
	private String mobile;	// 法人手机号
	@Column(name="id_card")
	private String idCard;	//身份证号
	@Column(name="peovince_id")
	private String peovinceId;	//商户经营地址 - 省
	@Column(name="city_id")
	private String cityId;	//商户经营地址 -市
	@Column(name="area_code")
	private String areaCode;	//商户经营地址-区
	@Column(name="business_address")
	private String businessAddress;	//商户经营地址-详细
	@Column(name="bank_id")
	private String bankId;	//结算开户行
	@Column(name="branchbank_id")
	private String branchbankId;	//结算开户支行
	@Column(name="branchbank_name")
	private String branchbankName;	//结算开户支行名称
	@Column(name="account_name")
	private String accountName;	// 结算账户名称
	@Column(name="account_no")
	private String accountNo;	//结算账户账号
	
	
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
	
//---------------------------------------------------------------------------暂没用	
	//顶级代理商ID
	@Column(name="topagent_id")
	private String topAgentId;
	//顶级代理商name
	@Column(name="topagent_name")
	private String topAgentName;
	@Column(name="scobus")
	private String scobus;	// 经营范围
	@Column(name="merlinkman")
	private String merlinkman;	//商户联系人
	@Column(name="merlinkmobile")
	private String merlinkmobile;
	@Column(name="email")
	private String email;
	@Column(name="qq")
	private String qq;
	@Column(name="description")
	private String description;	// 申请的设备描述
    @Column(name="fee_rate",precision=8,scale=2)
    private Double feeRate;	// 借记卡扣率(%)
    @Length(max=10)
    @Column(name="uplmt")
    private Double uplmt;	// 借记卡封顶
    @Column(name="dfee_rate",precision=8,scale=2)
    private Double dfeeRate;	// 贷记卡扣率(%)
    @Column(name="note")
    private String note;	// 备注
    @Column(name="apply_name")
    private String applyName;	//申请人
    @Column(name="inscode")
    private String inscode;
    @Column(name="single_fee")
    private Double  singleFee;
    @Column(name="wechat_rate")
    private Double  wechatRate;
    @Column(name="short_name")
    private String  shortName;
    @Column(name="organization_no")
    private String  organizationNo;
    @Column(name="taxcertificate_no")
    private String  taxcertificateNo;
    
    @ApiModelProperty("二维码OSS地址")
    @Column(name="qr_code_addr")
    private String qrCodeAddr;
    
    @ApiModelProperty("二维码标识")
    @Column(name="qr_code_flag")
    private Integer qrCodeFlag;
}

