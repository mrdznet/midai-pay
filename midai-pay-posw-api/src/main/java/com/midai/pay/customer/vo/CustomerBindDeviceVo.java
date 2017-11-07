package com.midai.pay.customer.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class CustomerBindDeviceVo implements Serializable{
	
	private static final long serialVersionUID = 2019004271927968447L;
	private String deviceNo;
	private String typeName;
	private String modeName;
	private String isFirst;
}
