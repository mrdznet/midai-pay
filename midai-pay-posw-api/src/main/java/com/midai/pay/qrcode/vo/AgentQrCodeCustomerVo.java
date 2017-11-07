package com.midai.pay.qrcode.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class AgentQrCodeCustomerVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("二维码ID")
	private String qrCodeId;
	@ApiModelProperty("所属商户编码")
	private String mercNo;
	@ApiModelProperty("所属商户名称")
	private String mercName;
	@ApiModelProperty("所属代理商")
	private String agentNo; 
	@ApiModelProperty("二维码链接")
	private String qrcodePath; 
	@ApiModelProperty("Id")
	private String id; 
	@ApiModelProperty("二维码名称")
	private String fileName; 
}
