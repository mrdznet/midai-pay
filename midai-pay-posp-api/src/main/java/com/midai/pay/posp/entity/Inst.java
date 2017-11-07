package com.midai.pay.posp.entity;

import javax.persistence.Id;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table(name = "tbl_inst")
@EqualsAndHashCode(callSuper=false)
public class Inst extends BaseEntity {

	@Id
	private String instCode;

	private String instName;

	private String tpdu;

	private String msgHead;

	private String instIp;

	private int instPort;

	private double instFeeRate;

	private int instFeeMax;

	private double instDfreeRate;
	
	private int keyMode;

}
