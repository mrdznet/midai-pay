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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

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
@Table(name="tbl_bo_customer_review")
public class BoCustomerReview extends BaseEntity implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
	@Id
	private Integer id;
	
	/**
	 * 商户编号
	 */
	@Column(name="merc_no")
	private String mercNo;
	
	/**
	 * 级别:0:初审  1:风控
	 */
	@Column(name="review_level")
	private Integer reviewLevel;
	
	/**
	 * 审核结果 1:同意  0:退回
	 */
	@Column(name="review_result")
	private Integer reviewResult;
	
	/**
	 * 审核意见
	 */
	@Column(name="advice")
	private String advice;
	
	@Column(name="create_time")
	private Date createTime;
	
	@Column(name="update_time")
    private Date updateTime;
	
	@Column(name="oper_user")
	private String operUser;
    
	private String taskId;
	
	private String  customerLevel;	//商户等级
}

