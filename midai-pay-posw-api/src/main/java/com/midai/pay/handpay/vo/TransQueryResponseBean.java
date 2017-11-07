package com.midai.pay.handpay.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class TransQueryResponseBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String merSysId; // 机构号
	private String subMerId; // 商户编号
	
	private String orderNo; //订单号
	private String transDate; //交易日期
	private String transStatus; //交易状态
	private String transAmount;	//T0提现申请金额(单位:分)
	private String actualAmount;	//T0提现到账金额(单位:分)
	private Integer transSeq;	//翰鑫流水号
	
	private String code;	//返回状态码
	private String msg;	//返回状态描述
	private String signature;	//签名信息
}
