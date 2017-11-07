package com.midai.pay.route.service;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.midai.pay.inst.entity.InstAll;
import com.midai.pay.routehistory.entity.HistoryData;

public interface HistoryDataService {

	List<HistoryData> getChannelHistoryData(Set<Entry<String, InstAll>> entrySet);

}
