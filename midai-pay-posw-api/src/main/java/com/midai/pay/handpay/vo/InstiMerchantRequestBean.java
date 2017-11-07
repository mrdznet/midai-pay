package com.midai.pay.handpay.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 商户进件请求Bean
 * @author Feng
 *
 */

@Data
public class InstiMerchantRequestBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String merSysId; // 机构号
	private String subMerId; //商户编号
	private String merName;	// 商户名称
	private String realName;	// 姓名
	private String merState;	// 商户所在省份
	private String merCity;	// 商户所在城市
	private String merAddress;	// 商户所在详细地址
	
	private String certType;	// 证件类型: (01-身份证, 02-军官证, 03-护照, 04-户口簿, 05-回乡证, 06-其他)
	private String certId;	// 证件号
	
	private String mobile; // 手机号
	private String accountId;	// 结算账号
	private String accountName;	// 结算户名
	private String bankName;	// 总行名称
	private String bankCode;	// 总行联行号
	private String openBankName;	//(非必填) 开户行全称
	private String openBankCode;	//(非必填) 开户行联行号
	private String openBankState;	//(非必填) 开户行省份
	private String openBankCity;	//(非必填) 开户行城市
	
	private String posCertIdImage;	//(非必填) 身份证正面照片
	private String backCertIdImage;	//(非必填) 身份证反面照片
	private String handCertIdImage;	//(非必填) 手持身份证照片
	private String firBuzAreaImage;	//(非必填) 营业场所照片一
	private String secBuzAreaImage;	//(非必填) 营业场所照片二
	private String buzLicenImage;	//(非必填) 营业执照照片
	private String openPerImage;	//(非必填) 开户许可证照片
	
	private String t0drawFee;	//单笔T0提现交易手续费: 如0.2元/笔则填0.2
	private String t0drawRate;	//T0提现交易手续费扣率: 如0.6%笔则填0.006
	private String t1consFee;	//单笔消费交易手续费: 如0.2元/笔则填0.2
	private String t1consRate;	//消费交易手续费扣率: 如0.6%笔则填0.006
	
	/**
	 * 将参与签名的字段内容按顺序连接起来，组成一个签名字符串明文，通过机构私钥使用MD5加签。参与签名的字段及顺序为：
	 * merSysId|merName|realName|merState|merCity|merAddress|certType|certId|mobile|accountId|accountName
	 * |bankName|bankCode|openBankName|openBankState|openBankCity|t0drawFee|t0drawRate|t1consFee|t1consRate|signKey
	 * 
	 * 注意：空值则为空的字符串
	 * 
	 */
	private String signature; //签名信息
	
}
