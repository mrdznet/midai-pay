package com.midai.pay.customer.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="tbl_bo_customer_inst")
public class BoCustomerInst extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mercNo;
	
	private String instCode;
}
