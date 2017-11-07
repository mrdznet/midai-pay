package com.midai.pay.posp.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class ConSumptionResult implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 4461394534946055234L;
	
	  private String RSPCOD;
	  
	  private String RSPMSG;
	  
	  private String LOGNO;
	  
	  private String TTXNDT;

}
