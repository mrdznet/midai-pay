package com.midai.pay.customer.service.impl;

import java.util.List;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.customer.entity.BoCustomerReview;
import com.midai.pay.customer.mapper.BoCustomerReviewMapper;
import com.midai.pay.customer.service.BoCustomerReviewService;

@Service
public class BoCustomerReviewServiceImpl extends BaseServiceImpl<BoCustomerReview> implements BoCustomerReviewService {

	private final BoCustomerReviewMapper reviewMapper;
	
	public BoCustomerReviewServiceImpl(BoCustomerReviewMapper mapper) {
		super(mapper);
		
		this.reviewMapper = mapper;
	}

	@Override
	public BoCustomerReview findLatestReview(String mercNo, int reviewLevel) {
		
		return reviewMapper.findLatestReview(mercNo, reviewLevel);
	}

	@Override
	public List<BoCustomerReview> findReviewsByLevel(String mercNo, int reviewLevel) {
		
		return reviewMapper.findReviewsByLevel(mercNo, reviewLevel);
	}

	@Override
	public int insertCustomerReview(BoCustomerReview review) {
		return reviewMapper.insertCustomerReview(review);
	}


}
