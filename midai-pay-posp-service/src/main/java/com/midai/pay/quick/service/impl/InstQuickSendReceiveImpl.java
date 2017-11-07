package com.midai.pay.quick.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.quick.mapper.InstQuickSendReceiveMapper;
import com.midai.pay.quick.qb.entity.InstQuickSendReceiveEntity;
import com.midai.pay.quick.qb.service.InstQuickSendReceiveService;

@Service
public class InstQuickSendReceiveImpl implements InstQuickSendReceiveService {

	@Autowired
	private InstQuickSendReceiveMapper mapper;
	
	@Override
	public List<InstQuickSendReceiveEntity> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InstQuickSendReceiveEntity findByPrimaryKey(Object key) {
		
		return null;
	}

	@Override
	public int insert(InstQuickSendReceiveEntity t) {
		mapper.insertUseGeneratedKeys(t);
		return t.getId();
	}

	@Override
	public int deleteByPrimaryKey(Object key) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(InstQuickSendReceiveEntity t) {
		return mapper.updateByPrimaryKeySelective(t);
	}

	@Override
	public InstQuickSendReceiveEntity findOne(InstQuickSendReceiveEntity t) {
		// TODO Auto-generated method stub
		return null;
	}

}
