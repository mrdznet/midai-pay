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
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月21日  <br/>
 * @author   zt
 * @version  
 * @since    JDK 1.7
 * @see 	 
*/




@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="tbl_deal_total")
public class  Dealtotal  extends BaseEntity implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	  /*流水号*/  
	@Id
	  @Column(name= "host_trans_ssn",length=12,unique =true)	 	  
	  @NotNull
 	  private  String hostTransSsn; 

	  @Column(name="pay_mode")
	  private String payMode;
	  
	  /*返回码*/
	  @Column(name="resp_cd_loc")
	  private String respCdLoc;
	  
	  /*返回信息*/
	  @Column(name ="resp_cd_loc_dsp")
	  private String respCdLocDsp;
	  
	  /*商户小票号*/
	  @Column(name="mchnt_code_in")
	  private String mchntCodeIn;
	  
	  /*商户名称*/
	  @Column(name="mchnt_name")
	  private String mchntName;
	  
	  /*商户手机号 */
	  @Column(name="mobile")
	  private String mobile;
	  
	  /*交易时间*/
      @Column(name="trans_time")
      private Date transTime;
      
      /*交易金额*/
      @Column(name="trans_amt")
      private Integer transAmt;
      
      /*银行卡号*/
      @Column(name="trans_card_no")
      private String transCardNo;
      
      /*机身号*/
      @Column(name="device_no_in")
      private String deviceNoIn;
      
      @Column(name="device_id_in")
      private String deviceIdIn;
      
      /*发卡行*/
      @Column(name ="card_iss_name")
      private String cardIssName;
      
      /*发卡行编号 */
      @Column(name="card_iss_id")
      private String cardIssId;
      
      @Column(name ="card_bin")
      private String cardBin;
      
      /*卡类型*/
      @Column(name="card_kind")
      private String cardKind;
      
      /*交易类型code*/
      @Column(name="trans_code")
      private String transCode;
      
      /*交易类型*/
      @Column(name="trans_code_name")
      private String transCodeName;
      
      /*交易状态*/
      @Column(name="trans_status")
      private Integer transStatus;
     
      /*商户类型*/
      @Column(name="special_fee_type")
      private String specialFeeType;
      
      /*大商户*/
      @Column(name="mchnt_code_out")
      private String mchntCodeOut;
      
      @Column(name="device_id_out")
      private String deviceIdOut;
      
      @Column(name="device_ssn_out")
      private String deviceSsnOut;
      
      /*通道机构*/
      @Column(name ="rout_inst_id_cd")
      private String routInstIdCd;
      
      /*代理商编号*/
      @Column(name="agent_id")
      private String agentId;
      
      /*代理商名称*/
      @Column(name="agent_name")
      private String agentName;
      
      /*商户扣率*/
      @Column(name="mchnt_rate" ,precision=8,scale=2)
      private Double mchntRate;
      
      @Column(name="mchnt_single_fee")
      private Integer mchntSingleFee;
      
      /*借记卡*/
      @Column(name="inst_fee_rate" ,precision=8,scale=2)
      private Double instFeeRate;
      
      /*借记卡封顶值*/
      @Column(name="inst_fee_max")
      private Integer instFeeMax;
      
      /*贷记卡*/
      @Column(name="inst_dfee_rate", precision=8,scale=2)
      private double instDfeeRate;
      
      
      @Column(name="sign_path")
      private String signPath;
      
      @Column(name="eticket_path")
      private String eticketPath;
      
      @Column(name="inscode")
      private String inscode;
      
	  private Date createTime;
	  private Date updateTime;					

}
