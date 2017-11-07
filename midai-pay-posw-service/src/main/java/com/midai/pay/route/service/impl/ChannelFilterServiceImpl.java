package com.midai.pay.route.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.inst.entity.InstAll;
import com.midai.pay.route.service.ChannelFilterService;
import com.midai.pay.routeregulation.entity.TranscationRegulationEntity;
import com.midai.pay.routetransactionInformation.TransactionInformation;

@Service
public class ChannelFilterServiceImpl implements ChannelFilterService {

	@Override
	public Set<InstAll> getFilterChannelList(TransactionInformation tsv, List<TranscationRegulationEntity> treList,
			Map<String, InstAll> map) {

		Set<InstAll> set = new HashSet<InstAll>();
		Set<String> keys = map.keySet();
		for(String key : keys) {
			set.add(map.get(key));
		}
		return set;
	}

}
