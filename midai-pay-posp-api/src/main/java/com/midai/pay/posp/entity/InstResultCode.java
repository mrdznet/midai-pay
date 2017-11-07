/**
 * Project Name:midai-pay-posp-api
 * File Name:InstResultCode.java
 * Package Name:com.midai.pay.posp.entity
 * Date:2016年11月15日上午10:44:35
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.posp.entity;

import javax.persistence.Id;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ClassName:InstResultCode <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年11月15日 上午10:44:35 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Data
@Table(name = "tbl_inst_result_code")
@EqualsAndHashCode(callSuper=false)
public class InstResultCode extends BaseEntity {
	
	@Id
	private int id;
	
	private String code;
	
	private String message;
	
	private String instCode;

}

