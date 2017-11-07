package com.midai.pay.mobile.vo;

import lombok.Data;

@Data
public class DeviceCustomerVo {

	private String deviceid;
	private Integer cdid;
	private String mercno;
	private Integer state;
	private Integer sboid;
	private String bodyno;
	
	private String mercid;
	private String customerid;
	private String agentid;
	private String agentname;
}
