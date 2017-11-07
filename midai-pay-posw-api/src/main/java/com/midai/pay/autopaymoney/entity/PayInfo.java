package com.midai.pay.autopaymoney.entity;

import lombok.Data;

@Data
public class PayInfo {
	
	//结算信息
	private String mercId;	 	//	商户号
	private String tixiLogno;	//提现流水号
	private Integer payMoney;	//打款金额
	private String bankName;	//开户行
	private String accountName;	//开户名
	private String accountNo;	//银行卡号
	
	//商户信息
	private String bankId;	//总行行号-清算行号
	private String branchbankId;	////支行行号-开户行号
	private String bkName;	//总行名称
}
