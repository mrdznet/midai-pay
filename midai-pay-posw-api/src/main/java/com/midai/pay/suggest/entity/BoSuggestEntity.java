/**
 * Project Name:chenxun-framework-start
 * File Name:User.java
 * Package Name:com.chenxun.framework.entity
 * Date:2016年9月1日上午10:12:33
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.suggest.entity;

import java.io.Serializable;

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
@Table(name="tbl_bo_suggestion")
public class BoSuggestEntity extends BaseEntity implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
	@Id
	private Integer id;
	/**
	 *创建人
	 */
	@Column(name="create_user")
	private String createUser;
	/**
	 *更新人
	 */
	@Column(name="update_user")
	private String updateUser;

	@Column(name="advice_name")
	private String adviceName;
	
	@Column(name="advice_phone")
	private String advicePhone;

	@Column(name="advice_content")
	private String adviceContent;
	
	@Column(name="depart_name")
	private String departName;
	
	@Column(name="urgency")
	private String urgency;

	@Column(name="answer")
	private String answer;
	
	@Column(name="answer_name")
	private String answerName;

	@Column(name="state")
	private Integer state;

}

