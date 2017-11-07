package com.midai.pay.trade.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="tbl_deal_total")
public class DealTotal extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="host_trans_ssn")
	private String hostTransSsn;
	
	/**
	 * '1'  'mpos' ,  '2'  '无卡 ',  '3'    '大pos' 
	 */
	@Column(name="pay_mode")
	private String payMode;
	
	@Column(name="resp_cd_loc")
	private String respCdLoc;
	
	@Column(name="resp_cd_loc_dsp")
	private String respCdLocDsp;
	
	@Column(name="mchnt_code_in")
	private String mchntCodeIn;
	
	@Column(name="mchnt_name")
	private String mchntName;
	
	@Column(name="mobile")
	private String mobile;
	
	@Column(name="trans_time")
	private Date transTime;
	
	@Column(name="trans_amt")
	private Integer transAmt;
	
	@Column(name="trans_card_no")
	private String transCardNo;
	
//	@Column(name="term_app_id")
//	private String termAppId;
//	
	@Column(name="card_iss_name")
	private String cardIssName;
	
	@Column(name="card_iss_id")
	private String cardIssId;
	
	@Column(name="trans_code")
	private String transCode;
	
	@Column(name="trans_code_name")
	private String transCodeName;
	
	@Column(name="trans_status")
	private int transStatus;
	
	@Column(name="special_fee_type")
	private String specialFeeType;
	
	@Column(name="mchnt_code_out")
	private String mchntCodeOut;
	
	@Column(name="rout_inst_id_cd")
	private String routInstIdCd;
	
	@Column(name="agent_id")
	private String agentId;
	
	@Column(name="agent_name")
	private String agentName;
	

	
	@Column(name="sign_path")
	private String signPath;
	
	@Column(name="eticket_path")
	private String eticketPath;
	
	@Column(name="device_no_in")
	private String deviceNoIn;
	
	@Column(name="device_id_in")
	private String deviceIdIn;
	
	@Column(name="device_id_out")
	private String deviceIdOut;
	
	@Column(name="device_ssn_out")
	private String deviceSsnOut;
	
	@Column(name="mchnt_rate")
	private double mchntRate;
	
	@Column(name="mchnt_single_fee")
	private int mchntSingleFee;
	
	@Column(name="inst_fee_rate")
	private double instFeeRate;
	
	@Column(name="inst_fee_max")
	private int instFeeMax;
	
	@Column(name="inst_dfee_rate")
	private double instDfeeRate;
	
	@Column(name="card_bin")
	private String cardBin ;
	
	@Column(name="card_kind")
	private String cardKind;
	
	@Column(name="mishuai_seq")
	private String mishuaiSeq;
	
	@Column(name="merc_no")
	private String mercNo;
	
	private String inscode;
}
