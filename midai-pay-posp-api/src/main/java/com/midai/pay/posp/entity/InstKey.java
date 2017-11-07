package com.midai.pay.posp.entity;

import javax.persistence.Id;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table(name="tbl_inst_key")
@EqualsAndHashCode(callSuper=false)
public class InstKey extends BaseEntity {

	@Id
	private String instCode;

	private int sekId;

	private String priEncKey;

	private String pinEncKey;

	private String tckEncKey;

	private String macEncKey;
	
	private String deviceId;

}
