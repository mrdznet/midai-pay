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
@Table(name="tbl_bo_customer_device")
public class BoCustomerDevice extends BaseEntity implements Serializable{
    
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
	 * 小票号
	 */
	@Column(name="merc_id")
	private String mercId;
	
	/**
	 * 机身号
	 */
	@Column(name="body_no")
	private String bodyNo;
	
	/**
	 * 状态: 1绑定, 0解绑
	 */
	private Integer state;
	
	/**
	 * 绑定时间
	 */
	@Column(name="bunding_time")
	private Date bundingTime;
	
	/**
	 * 解绑时间
	 */
	@Column(name="unbunding_time")
    private Date unbundingTime;
	
	/**
	 * 设备的unid
	 */
	@Column(name="device_id")
	private String deviceId;
	
	/**
	 * 首选项：1:首选|0:否
	 */
	@Column(name="is_first")
	private Integer isFirst;
    
	@Column(name="create_time")
	private Date createTime;
	
	@Column(name="update_time")
    private Date updateTime;
    
}

