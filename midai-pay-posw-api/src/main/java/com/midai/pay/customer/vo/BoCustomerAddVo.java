package com.midai.pay.customer.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

import com.midai.pay.customer.entity.BoCustomer;
import lombok.Data;

@Data
@ApiModel
public class BoCustomerAddVo implements Serializable{
	private static final long serialVersionUID = 1L;
   
	private BoCustomer boCustomer;
	
	@ApiModelProperty(value="oss上传图片")
	private List<CustomerImgVo> imgList;
	
	private String deviceStr;
}
