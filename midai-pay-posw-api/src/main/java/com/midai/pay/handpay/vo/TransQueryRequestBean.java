package com.midai.pay.handpay.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class TransQueryRequestBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String merSysId; // 机构号
	private String subMerId; // 商户编号
	
	private String orderNo; //订单号
	private String transDate; //交易日期
	private String transSeq;	//翰鑫流水号
	
	private String signature;	//签名信息
	
}
