package com.midai.pay.quick.qb.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name="tbl_inst_quick_config")
@EqualsAndHashCode(callSuper=false)
@Data
public class InstQuickSendReceiveEntity extends BaseEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer Id;
	private String reqMsg;
	private String respMsg;
	private String instCode;
	

}
