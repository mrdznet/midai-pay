package com.midai.pay.device.vo;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel
@Data
@EqualsAndHashCode(callSuper = false)
public class BoIostorageCustomerVo  implements Serializable{


	private String mercName;
	private String destmercId;
	private String mercId; 
	private String deviceNo;
	private String createTime;
	private String state;
	private String updateTime;
	
}
