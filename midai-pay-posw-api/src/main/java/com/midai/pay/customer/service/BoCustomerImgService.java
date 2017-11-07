package com.midai.pay.customer.service;

import com.midai.framework.common.BaseService;
import com.midai.pay.customer.entity.CustomerImg;

public interface BoCustomerImgService extends BaseService<CustomerImg> {

	CustomerImg getByMercNoAndType(String mercNo, Integer id);
	
}
