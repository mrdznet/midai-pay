package com.midai.pay.customer.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.customer.entity.BoCustomerReview;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class CustomerSecondReviewVo {
	
	private BoCustomer customer;	//商户信息
	
	private List<BoCustomerReview> firstRreviewList;	// 初审意见
	
	private List<CustomerImgVo> imgs;	//图片信息
	
	private String branchBankName;
	
	private String instCode;
}
