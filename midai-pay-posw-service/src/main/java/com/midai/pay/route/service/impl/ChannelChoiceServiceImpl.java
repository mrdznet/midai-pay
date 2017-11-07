package com.midai.pay.route.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.inst.entity.InstAll;
import com.midai.pay.inst.query.InstAllQuery;
import com.midai.pay.inst.service.InstAllService;
import com.midai.pay.route.service.ChannelChoiceService;
import com.midai.pay.route.service.ChannelFilterService;
import com.midai.pay.route.service.ChannelWeightFactoryService;
import com.midai.pay.routehistory.entity.HistoryData;
import com.midai.pay.routehistory.service.HistoryDataService;
import com.midai.pay.routeregulation.entity.TranscationRegulationEntity;
import com.midai.pay.routeregulation.service.TranscationRegulationService;
import com.midai.pay.routetransactionInformation.TransactionInformation;

@Service
public class ChannelChoiceServiceImpl implements ChannelChoiceService {

	@Autowired
	private TranscationRegulationService transcationRegulationService;
	@Autowired
	private ChannelFilterService  channelFilterService;
	@Reference
	private InstAllService instAllService;
	@Autowired
	private ChannelWeightFactoryService  channelWeightFactoryService;
	@Autowired
	private HistoryDataService historyDataService;
	
	@Override
	public Set<String> getChannelList(TransactionInformation tsv) {
		Set<String> resultSet = null;
		//获取所有开启通道
		InstAllQuery instAllQuery = new InstAllQuery();
		instAllQuery.setInstState(0);
		instAllQuery.setOffset(1000);
		instAllQuery.setServiceType(2);
		List<InstAll> instList = instAllService.instAllList(instAllQuery);
		if(instList != null && instList.size() > 0) {
			//获取所有有效通道规则
			List<TranscationRegulationEntity> treList = transcationRegulationService.getAllValidTranscationRegulation();
			Map<String, InstAll> map = instListToMap(instList);
			//过滤符合条件的通道列表
			Set<InstAll> filterList = channelFilterService.getFilterChannelList(tsv, treList, map);
			
			if(filterList.size() <= 1) {
				Iterator<InstAll>  it = filterList.iterator();
				resultSet = new HashSet<String>();
				InstAll ia = it.next(); 
				resultSet.add(ia.getInstCode());
			} else {
				//获取过滤后通道列表对应的历史数据
				List<HistoryData> hdList = historyDataService.getChannelHistoryData(map.entrySet());
				resultSet = channelWeightFactoryService.getResultInstAllList(tsv, filterList, hdList);
			}
		} else {
			throw new RuntimeException("通道都已关闭");
		}
		return resultSet;
	}
	
	private Map<String, InstAll> instListToMap(List<InstAll> instList) {
		Map<String, InstAll> map = new HashMap<String, InstAll>();
		for(InstAll ia : instList) {
			map.put(ia.getInstCode(), ia);
		}
		
		return map;
	}

}
