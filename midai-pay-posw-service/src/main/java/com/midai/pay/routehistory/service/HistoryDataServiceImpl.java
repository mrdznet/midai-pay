package com.midai.pay.routehistory.service;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.inst.entity.InstAll;
import com.midai.pay.routehistory.entity.HistoryData;
import com.midai.pay.routehistory.service.HistoryDataService;

@Service
public class HistoryDataServiceImpl implements HistoryDataService {

	@Override
	public List<HistoryData> getChannelHistoryData(Set<Entry<String, InstAll>> entrySet) {

		return null;
	}

}
