package com.midai.pay.routeregulation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.routeregulation.entity.TranscationRegulationEntity;
import com.midai.pay.routeregulation.mapper.TranscationRegulationMapper;

import tk.mybatis.mapper.entity.Example;

@Service
public class TranscationRegulationServiceImpl implements TranscationRegulationService {

	@Autowired
	private TranscationRegulationMapper mapper;
	
	@Override
	public List<TranscationRegulationEntity> getAllValidTranscationRegulation() {
		
		Example example = new Example(TranscationRegulationEntity.class);
		example.createCriteria().andCondition("status", 1);
//		return mapper.selectByExample(example);
		return null;

	}

}
