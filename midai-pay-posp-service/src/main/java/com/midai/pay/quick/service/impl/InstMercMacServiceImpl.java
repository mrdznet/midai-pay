package com.midai.pay.quick.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.quick.mapper.InstMercMacMapper;
import com.midai.pay.quick.qb.entity.InstMercMacEntity;
import com.midai.pay.quick.qb.service.InstMercMacService;

@Service
public class InstMercMacServiceImpl implements InstMercMacService {

	@Autowired
	private InstMercMacMapper mapper;
	
	@Override
	public List<InstMercMacEntity> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InstMercMacEntity findByPrimaryKey(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insert(InstMercMacEntity t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteByPrimaryKey(Object key) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(InstMercMacEntity t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public InstMercMacEntity findOne(InstMercMacEntity t) {
		return mapper.selectOne(t);
	}

}
