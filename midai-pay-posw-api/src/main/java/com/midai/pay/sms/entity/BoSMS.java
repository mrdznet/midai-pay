package com.midai.pay.sms.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="tbl_bo_sms")
public class BoSMS extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer id;
	
	private String phonenumber;
	
	private String smscode;
	
	private String smscontent;
	
	private Integer stype;
	
	private Integer state;
	
}
