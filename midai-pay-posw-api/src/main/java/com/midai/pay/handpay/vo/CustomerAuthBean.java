package com.midai.pay.handpay.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class CustomerAuthBean implements Serializable{
	
	private String bankCard;//银行卡号
	private String loginTel;//用户登录手机号
}
