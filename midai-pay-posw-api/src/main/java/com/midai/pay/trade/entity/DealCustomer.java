package com.midai.pay.trade.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class DealCustomer implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//交易表
	private String hostTransSsn;	//流水号
	private Date transTime;		//交易时间
	private Integer transAmt;	//交易金额
	private String carType;	//卡类型(01:借记卡, 02:贷记卡)
	
	// 商户表
	private String mercId;	//小票号
	private Double mchntRate; // 商户扣率(%)
	private Integer mchntSingleFee;	//单笔手续费
}
