package com.midai.pay.customer.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="tbl_bo_customer_img")
public class CustomerImg extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	/**
	 * 商户编号
	 */
	@Column(name="merc_no")
	private String mercNo;
	
	private String url;
	
	private Integer type;
}
