package com.midai.pay.inst.entity;

import javax.persistence.Id;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table(name = "tbl_inst")
@EqualsAndHashCode(callSuper=false)
public class InstAll extends BaseEntity {

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
	
	private String  instTerminalNumber; 
	
	private int instCardType;
	
	private int instRouteSupport;
	
	private Double instPlatformRatio;
	
	private Double instMinCost;
	
	private Double instT0Factorage;
	
	private Double instD1Factorage;
	
	private Double instLimit;
	
	private int instState;
	
	private int instFactorageState;
	
	private int instFactorageMax;

}
