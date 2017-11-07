package com.midai.pay.route.service;

import java.util.List;
import java.util.Set;

import com.midai.pay.inst.entity.InstAll;
import com.midai.pay.routehistory.entity.HistoryData;
import com.midai.pay.routetransactionInformation.TransactionInformation;

public interface ChannelWeightFactoryService {

	Set<String> getResultInstAllList(TransactionInformation tsv, Set<InstAll> filterList,
			List<HistoryData> hdList);

}
