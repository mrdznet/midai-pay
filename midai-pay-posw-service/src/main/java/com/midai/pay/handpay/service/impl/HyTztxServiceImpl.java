package com.midai.pay.handpay.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.handpay.entity.HyTztxEntity;
import com.midai.pay.handpay.mapper.HyTztxMapper;
import com.midai.pay.handpay.query.HyTztxQuery;
import com.midai.pay.handpay.service.HyTztxService;

@Service
public class HyTztxServiceImpl extends BaseServiceImpl<HyTztxEntity> implements HyTztxService {

	private Logger logger = LoggerFactory.getLogger(HyTztxServiceImpl.class);

	private final HyTztxMapper mapper;

	public HyTztxServiceImpl(HyTztxMapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}

	@Override
	public List<HyTztxEntity> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HyTztxEntity findByPrimaryKey(Object key) {
		return mapper.selectByPrimaryKey(key);
	}

	@Override
	public int insert(HyTztxEntity t) {
		return mapper.insert(t);
	}

	@Override
	public int deleteByPrimaryKey(Object key) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(HyTztxEntity t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public HyTztxEntity findOne(HyTztxEntity t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HyTztxEntity> query(HyTztxQuery query) {
		
		return mapper.selectListByQuery(query);
	}

	@Override
	public int queryCount(HyTztxQuery query) {
		return mapper.selectCountByQuery(query);
	}

}
