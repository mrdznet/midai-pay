package com.midai.pay.quick.qb.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name="tbl_inst_quick_config")
@EqualsAndHashCode(callSuper=false)
@Data
public class InstQuickConfigEntity extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer Id;
	@Length(max=12)
	private String instCode;
	@Length(max=10)
	private String transType;
	@Length(max=2)
	private String payPass;
	@Length(max=150)
	private String url;
	@Length(max=50)
	private String organizationId;
	
}
