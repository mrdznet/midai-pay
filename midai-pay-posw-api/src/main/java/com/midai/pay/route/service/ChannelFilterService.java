package com.midai.pay.route.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.midai.pay.inst.entity.InstAll;
import com.midai.pay.routeregulation.entity.TranscationRegulationEntity;
import com.midai.pay.routetransactionInformation.TransactionInformation;

public interface ChannelFilterService {

	Set<InstAll> getFilterChannelList(TransactionInformation ts, List<TranscationRegulationEntity> treList,
			Map<String, InstAll> map);

}
