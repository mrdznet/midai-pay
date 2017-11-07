package com.midai.pay.handpay.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class WithdrawRequestBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String merSysId; // 机构号
	private String subMerId; // 商户编号
	
	private String orderNo; //订单号
	private String transDate; //交易日期
	private String transAmount; //T0提现申请金额: 交易金额(单位:分), 该字段为空时对商户余额全部提现，非空时对提交交易金额提现
	
	/**
	 * 将参与签名的字段内容按顺序连接起来，组成一个签名字符串明文，使用机构密钥进行MD5加签后进行验签。参与签名的字段及顺序为：
	 * merSysId|subMerId|orderNo|transDate|transAmount|signKey
	 * 
	 * 注意：空值则为空的字符串
	 */
	private String signature; // 签名信息

}
