package com.midai.pay.user.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="tbl_bank")
public class Bank extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String bankcode;
	
	private String bankname;
	
	private String showname;
	
	private String shortcode;
}
