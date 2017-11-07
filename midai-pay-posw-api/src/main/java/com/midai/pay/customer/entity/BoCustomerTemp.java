package com.midai.pay.customer.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.midai.framework.common.BaseEntity;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="tbl_bo_customer_temp")
public class BoCustomerTemp  extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int id;
	/**
	 * 商户编号
	 */
	private String mercNo;
	/**
	 * 商户小票号或者商户号
	 */
	private String mercId;
	private String mercName;
	private String mobile;
	private String accountName;
	private String accountNo;
	private String bankId;
	private String branchBankName;
	private String branchBankId;
	private String picPath;
	/**
	 * 状态:0:申请中 |1:初审中 | 2:未通过 |3:审核通过
	 */
	private int state;
	/**
	 * 处理状态: 0:未处理, 1:已处理
	 */
    private int delState;
    
    private String note;
    private String advice;
    /**
     * 审核意见： 0同意   1拒绝
     */
    private int reviewResult;
    private String provinceId;
    private String cityId;
    private String confirmaccountNo;
    
    private String taskId;
}
