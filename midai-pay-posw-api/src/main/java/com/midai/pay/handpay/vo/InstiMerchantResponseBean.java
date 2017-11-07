package com.midai.pay.handpay.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 商户进件响应Bean
 * 
 * @author Feng
 *
 */

@Data
public class InstiMerchantResponseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String merSysId; // 机构号
	
	private String subMerId; // 商户编号
	private String tsn; //终端号
	
	private String code; //返回状态码
	private String msg; //返回状态描述
	
	private String signature; //签名信息
}
