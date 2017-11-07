package com.midai.pay.autopaymoney.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自动打款
 * @author miaolei
 * 2016年10月18日 上午9:24:36
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="tbl_bo_auto_paymoney")
public class BoAutoPayMoney extends BaseEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -76214659831686939L;

	/**
	 * 主键
	 */
	@Id
	private Integer id;
	
	/**
	 * 提现流水号
	 */
	@Column(name="tixi_logno", length=32)
	private String tixiLogno;
	
	/**
	 * 打款时间
	 */
	@Column(name="pay_time")
	private Date payTime;
	
	/**
	 * 打款人员
	 */
	@Column(name="pay_person", length=20)
	private String payPerson;
	
	/**
	 * 打款状态 0:待结算;1:结算中;3:结算成功;4:结算失败
	 */
	@Column(name="pay_state")
	private Integer payState;
	
	/**
	 * 通道返回码
	 */
	@Column(name="channel_code", length=32)
	private String channelCode;
	
	/**
	 * 商户号
	 */
	@Column(name="merc_id", length=20)
	private String mercId;
	
	/**
	 * 打款失败返回码
	 */
	@Column(name="error_code", length=50)
	private String errorCode;
	
	/**
	 * 打款失败原因
	 */
	@Column(name="error_msg", length=50)
	private String errorMsg;
	
	/**
	 * 打款金额
	 */
	@Column(name="pay_money")
	private Integer payMoney;
	
	/**
	 *  打款标识 1:自动打款;2:普通打款
	 */
	@Column(name="autohit_flag")
	private Integer autohitFlag;
	
	/**
	 * 开户行
	 */
	@Column(name="bank_name" , length=50)
	private String bankName;
	
	/**
	 * 开户名
	 */
	@Column(name="account_name" , length=20)
	private String accountName;
	
	/**
	 * 银行卡号
	 */
	@Column(name="account_no" , length=20)
	private String accountNo;
	
	/**
	 * 打款备注
	 */
	@Column(name="pay_remark" , length=120)
	private String payRemark;
	
	
	/**
	 * 银行编号
	 */
	@Column(name="bank_code" , length=20)
	private String bankCode;
	
	/**
	 * 打款通道
	 */
	@Column(name="pay_channel" , length=20)
	private String payChannel;
	
	/**
	 * 提现时间
	 */
	@Column(name="tixian_datetime" )
	private Date tixianDatetime;
	
	/**
	 * 商户名称
	 */
	@Column(name="merc_name" , length=20)
	private String mercName;
	
	/**
	 * 创建人
	 */
	@Column(name="create_user" , length=20)
	private String createUser;
	
}
