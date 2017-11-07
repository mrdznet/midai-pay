package com.midai.pay.user.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.user.entity.Mobileversion;
import com.midai.pay.user.mapper.MobileversionMapper;
import com.midai.pay.user.service.MobileversionService;

@Service
public class MobileversionServiceImpl extends BaseServiceImpl<Mobileversion> implements MobileversionService {

	private final MobileversionMapper mapper;
	
	public MobileversionServiceImpl(MobileversionMapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}

	@Override
	public Mobileversion getLastVersion(String device, Integer type) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("device", device);
		map.put("type", type.toString());
		return mapper.getLastVersion(map);
	}

	

}
