/**
 * Project Name:midai-pay-sercurity-api
 * File Name:SignEntity.java
 * Package Name:com.midai.pay.sercurity.entity
 * Date:2016年9月12日上午11:04:31
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
 */

package com.midai.pay.posp.entity;

import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.midai.framework.common.BaseEntity;

/**
 * ClassName:SignEntity <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年9月12日 上午11:04:31 <br/>
 * 
 * @author 陈勋
 * @version
 * @since JDK 1.7
 * @see
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
public class SignEntity extends BaseEntity {

	private String MACKEY;

	private String ENCRYPTKEY;

	private String PINKEY;

	private String PHONENUMBER;

	private String RSPCOD;

	private String TERMINALNUMBER;

	private String RSPMSG;

}
