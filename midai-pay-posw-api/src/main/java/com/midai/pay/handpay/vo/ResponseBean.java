package com.midai.pay.handpay.vo;

import lombok.Data;

@Data 
public class ResponseBean {

	private String merSysId;
	private String subMerId;
	private String orderNo;
	private String transDate;
	private String transStatus;
	private String transAmount;
	private String actualAmount;
	private String transSeq;
	private String signature;
	private String code;
	private String msg;
	
}
