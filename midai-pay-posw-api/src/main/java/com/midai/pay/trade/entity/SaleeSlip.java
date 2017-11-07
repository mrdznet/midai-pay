package com.midai.pay.trade.entity;

import lombok.Data;

/**
 * 签购单信息
 * 
 * @author Feng
 *
 */

@Data
public class SaleeSlip {
	private String mchntName; // 商户名称
	private String mercNo; // 商户编号
	private String deviceNoIn; // 终端编号
	private String optUser; // 操作员号 (01)
	private String transCardNo; // 卡号
	private String cardValid; // 卡有效期 (2000/00)
	private String accountName; // 发卡行
	private String batchNum; // 批次号 (000001)
	private String transCodeName; // 交易类型
	private String hostTransSsn; // 参考号
	private String deviceSsnOut; // 凭证号
	private String respAuthId;	//授权码(000001)
	private String transTime;	//时间/日期
	private Double transAmt;	//金额
	private String signPath;	// 签名图片地址
}
