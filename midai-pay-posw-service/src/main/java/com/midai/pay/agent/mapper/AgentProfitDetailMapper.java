package com.midai.pay.agent.mapper;

import java.util.List;

import org.apache.ibatis.annotations.SelectProvider;

import com.midai.pay.agent.query.AgentProfitQuery;
import com.midai.pay.agent.vo.AgentProfitStatisticsVo;
import com.midai.pay.agent.vo.AgentProfitVo;

public interface AgentProfitDetailMapper  {

	@SelectProvider(type=com.midai.pay.agent.provider.AgentProfitDetailProvider.class, method="findListByAgentProfitDetailQuery")
	List<AgentProfitVo> findListByAgentProfitDetailQuery(AgentProfitQuery query);

	@SelectProvider(type=com.midai.pay.agent.provider.AgentProfitDetailProvider.class, method="findCountByAgentProfitDetailQuery")
	Integer findCountByAgentProfitDetailQuery(AgentProfitQuery query);

	@SelectProvider(type=com.midai.pay.agent.provider.AgentProfitDetailProvider.class, method="queryAgentProfitDetailInfo")
	AgentProfitStatisticsVo queryAgentProfitDetailInfo(AgentProfitQuery query);
	
}
