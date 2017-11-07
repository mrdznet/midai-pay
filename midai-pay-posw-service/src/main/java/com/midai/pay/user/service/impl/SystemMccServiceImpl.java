package com.midai.pay.user.service.impl;

import java.util.List;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.user.entity.SystemChina;
import com.midai.pay.user.entity.SystemMCC;
import com.midai.pay.user.mapper.SystemMccMapper;
import com.midai.pay.user.service.SystemMccService;

import tk.mybatis.mapper.entity.Example;

@Service
public class SystemMccServiceImpl extends BaseServiceImpl<SystemMCC> implements SystemMccService {

	private final SystemMccMapper mapper;
	
	public SystemMccServiceImpl(SystemMccMapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}

	@Override
	public List<SystemMCC> loadData(String code) {
		Example ex = new Example(SystemChina.class);
		ex.createCriteria().andEqualTo("father", code);
		
		return mapper.selectByExample(ex);
	}

	@Override
	public SystemMCC loadById(int id) {
		return mapper.selectByPrimaryKey(id);
	}


}
