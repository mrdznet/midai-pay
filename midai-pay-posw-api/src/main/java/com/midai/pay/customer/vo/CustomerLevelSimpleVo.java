package com.midai.pay.customer.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class CustomerLevelSimpleVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="等级")
	private String level;
	
}
