package com.midai.pay.posp.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class MiShuaPayParam implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7347781904129239057L;

	private String cardNo;
	
	private String money;
	
	private String blankNo;
	
	private String name;
	
//	private String seqNo;
	
	private String tranSsn;
	 

}
