package com.midai.pay.customer.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.customer.entity.CustomerImg;
import com.midai.pay.customer.mapper.BoCustomerImgMapper;
import com.midai.pay.customer.service.CustomerImgService;

@Service
public class CustomerImgServiceImpl extends BaseServiceImpl<CustomerImg> implements CustomerImgService {

	private final BoCustomerImgMapper mapper;
	
	public CustomerImgServiceImpl(BoCustomerImgMapper mapper) {
		super(mapper);
		
		this.mapper = mapper;
	}

	
	

}
