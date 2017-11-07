package com.midai.pay.routeregulation.service;

import java.util.List;

import com.midai.pay.routeregulation.entity.TranscationRegulationEntity;

public interface TranscationRegulationService {

	/**
	 * 获取所有有效通道规则
	 * @return
	 */
	public List<TranscationRegulationEntity> getAllValidTranscationRegulation();
}
