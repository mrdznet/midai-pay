package com.midai.pay.agent.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.InsertProvider;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.agent.entity.AgentProfit;

public interface AgentProfitMapper extends MyMapper<AgentProfit> {

	@InsertProvider(type=com.midai.pay.agent.provider.AgentProfitProvider.class, method="batchInsert")
	int batchInsert(Map<String, List<AgentProfit>> map);

}
