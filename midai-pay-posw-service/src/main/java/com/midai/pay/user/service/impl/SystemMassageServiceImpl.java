package com.midai.pay.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.customer.mapper.BoCustomerMapper;
import com.midai.pay.mobile.utils.MassageUtil;
import com.midai.pay.user.service.SystemMassageService;

@Service
public class SystemMassageServiceImpl implements SystemMassageService {
	
	@Autowired
	private MassageUtil msgUtil;

	@Autowired
	BoCustomerMapper mapper;
	
	@Override
	public void sendMsgByRole(String pageMap, String mobile) {
		msgUtil.sendMsgByResource(pageMap, mapper.findInscodeByMobile(mobile));
	}
	
	
}
