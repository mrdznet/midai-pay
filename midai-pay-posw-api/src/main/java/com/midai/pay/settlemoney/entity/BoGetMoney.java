package com.midai.pay.settlemoney.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.midai.framework.common.BaseEntity;

/**
 * 
 * @author cjy
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="tbl_bo_getmoney")
public class BoGetMoney  extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -17180146147442988L;
	
	/**
	 * 流水号
	 */
	private String logNo;
	
	/**
	 * 商户小票号
	 */
	private String mercId;
	/**
	 * 提现日期
	 */
	private Date tixianDatetime;
	/**
	 * 提现金额
	 */
	private Integer tixianAmt;
	/**
	 * 提现手续费
	*/
	private Integer tixianFeeamt;
	/**
	 * 当前账户余额
	 */
	private Integer currentAmt;
	
	/**
	 * 经度
	 */
	private String latitude;
	/**
	 * 纬度
	 */
	private String longitude;
	/**
	 * 提现描述
	 */
	private String txdesc;
	/**
	 * 清算状态: 0:待审 1:审核通过
	 */
	private Integer settleState;
	/**
	 * 清算操作时间
	 */
	private Date settleDate;
	/**
	 * 清算操作人
	 */
	private String settlePerson;
	/**
	 * 提现扣率
	 */
	private Double feeratio;
	/**
	 * 审核状态 0:待审 2:审核通过 4:冻结
	 */
	private Integer checkState;
	
	private Date createTime;
	
}
