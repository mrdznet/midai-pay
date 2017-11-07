package com.midai.pay.customer.service;

import java.util.List;

import com.midai.framework.common.BaseService;
import com.midai.pay.customer.entity.BoCustomerReview;

public interface BoCustomerReviewService extends BaseService<BoCustomerReview> {
	
	int insertCustomerReview(BoCustomerReview review);
	
	BoCustomerReview findLatestReview(String mercNo, int reviewLevel);
	
	List<BoCustomerReview> findReviewsByLevel(String mercNo, int reviewLevel);
}
