package com.midai.pay.quick.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.quick.mapper.InstMercMacMapper;
import com.midai.pay.quick.qb.entity.InstQuickConfigEntity;
import com.midai.pay.quick.qb.service.InstQuickConfigService;

@Service
public class InstQuickConfigServiceImpl implements InstQuickConfigService {

	@Autowired
	private InstMercMacMapper mapper;

	@Override
	public List<InstQuickConfigEntity> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InstQuickConfigEntity findByPrimaryKey(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insert(InstQuickConfigEntity t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteByPrimaryKey(Object key) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(InstQuickConfigEntity t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public InstQuickConfigEntity findOne(InstQuickConfigEntity t) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
