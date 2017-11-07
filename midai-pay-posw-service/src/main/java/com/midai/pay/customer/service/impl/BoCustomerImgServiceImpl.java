package com.midai.pay.customer.service.impl;

import java.util.List;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.customer.entity.CustomerImg;
import com.midai.pay.customer.mapper.BoCustomerImgMapper;
import com.midai.pay.customer.service.BoCustomerImgService;

import tk.mybatis.mapper.entity.Example;

@Service
public class BoCustomerImgServiceImpl extends BaseServiceImpl<CustomerImg> implements BoCustomerImgService {

	private final BoCustomerImgMapper mapper;
	
	public BoCustomerImgServiceImpl(BoCustomerImgMapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}

	@Override
	public CustomerImg getByMercNoAndType(String mercNo, Integer id) {
		Example example= new Example(CustomerImg.class);
		List<CustomerImg> list = mapper.selectByExample(example);
		if(list != null && list.size() > 0) {
			list.get(0);
		}
		return null;
	}

}
