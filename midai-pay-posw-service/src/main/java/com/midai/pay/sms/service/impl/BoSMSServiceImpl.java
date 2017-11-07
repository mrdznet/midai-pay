package com.midai.pay.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.sms.entity.BoSMS;
import com.midai.pay.sms.mapper.BoSMSMapper;
import com.midai.pay.sms.service.BoSMSService;

@Service
public class BoSMSServiceImpl extends BaseServiceImpl<BoSMS> implements BoSMSService {

	private final BoSMSMapper mapper;
	
	public BoSMSServiceImpl(BoSMSMapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}

}
