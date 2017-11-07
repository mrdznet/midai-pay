package com.midai.pay.customer.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.customer.entity.BoCustomerReview;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class CustomerFirstReviewVo {
	
	private BoCustomer customer;
	
	private BoCustomerReview review;	// 审核信息
	
	private List<CustomerImgVo> imgs;	//图片信息
	
	private String taskId;
	
	private String instCode;
}
