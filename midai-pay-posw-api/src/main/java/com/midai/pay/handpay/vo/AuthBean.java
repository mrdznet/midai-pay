package com.midai.pay.handpay.vo;

import java.io.Serializable;

import lombok.Data;


@Data
public class AuthBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String accNo; //卡号
	private String certifId; //身份证号
	private String customerNm; //姓名
	private String phoneNo; //手机号
	private String loginPhone;//用户登录手机号
	private String mscode;	//短信验证码
	
	private String orderId; //商户订单号
	
}
