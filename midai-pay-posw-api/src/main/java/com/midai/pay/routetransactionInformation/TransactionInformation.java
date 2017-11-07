package com.midai.pay.routetransactionInformation;

import java.util.Date;

import lombok.Data;

@Data
public class TransactionInformation {

	
	/*** 商户手机号码  */
	private String moble;
	/*** 交易金额  */
	private Double money;
	/*** 交易卡号 */
	private String cardNo;
	/*** 交易时间 */
	private Date transcationTime;
	/*** 交易区域 */
	private String areaCode;
	
}
