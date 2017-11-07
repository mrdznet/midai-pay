package com.midai.pay.inst.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.inst.entity.InstMercAll;
import com.midai.pay.inst.mapper.InstMercAllMapper;
import com.midai.pay.inst.service.InstMercService;

@Service
public class InstMercServiceImpl implements InstMercService {

	@Autowired
	private InstMercAllMapper instMercMapper;
	
	@Override	
	public int insertInstMercAll(InstMercAll instMerc) {
		return instMercMapper.insertInstMercAll(instMerc);
	}

	@Override
	public int deleteInstMercAll(String instCode, String mercId) {
		return instMercMapper.deleteInstMercAll(instCode, mercId);
	}

	@Override
	public int selectInstMercAllCount(String instCode, String mercId) {
		return instMercMapper.selectInstMercAllCount(instCode, mercId);
	}

	@Override
	public InstMercAll selectInstMerc(String instJdCode, String mercId) {
		return instMercMapper.selectInstMerc(instJdCode, mercId);
	}

}
