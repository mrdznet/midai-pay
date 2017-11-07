package com.midai.pay.customer.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.customer.entity.BoCustomerInst;
import com.midai.pay.customer.mapper.BoCustomerInstMapper;
import com.midai.pay.customer.service.BoCustomerInstService;

@Service
public class BoCustomerInstServiceImpl implements BoCustomerInstService {

	@Autowired
	public BoCustomerInstMapper boCustomerInstMapper;
	
	@Override
	public int deleteCustomerInst(String mercNo) {
		return boCustomerInstMapper.deleteCustomerInst(mercNo);
	}

	@Override
	public int insertCustomerInst(String mercNo, String instCode) {
		return boCustomerInstMapper.insertCustomerInst(mercNo, instCode);
	}

	@Override
	public List<BoCustomerInst> selectCustomerInst(String mercNo) {
		return boCustomerInstMapper.selectCustomerInst(mercNo);
	}

}
