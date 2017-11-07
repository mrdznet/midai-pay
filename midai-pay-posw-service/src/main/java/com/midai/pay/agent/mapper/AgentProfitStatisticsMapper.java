package com.midai.pay.agent.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.SelectProvider;

import com.midai.pay.agent.query.AgentProfitQuery;
import com.midai.pay.agent.vo.AgentProfitStatisticsVo;
import com.midai.pay.agent.vo.AgentProfitVo;

public interface AgentProfitStatisticsMapper  {

	@SelectProvider(type=com.midai.pay.agent.provider.AgentProfitStatisticsProvider.class, method="findListByAgentProfitQuery")
	List<AgentProfitVo> findListByAgentProfitQuery(AgentProfitQuery query);

	@SelectProvider(type=com.midai.pay.agent.provider.AgentProfitStatisticsProvider.class, method="findCountByAgentProfitQuery")
	Integer findCountByAgentProfitQuery(AgentProfitQuery query);

	@SelectProvider(type=com.midai.pay.agent.provider.AgentProfitStatisticsProvider.class, method="updateAgentProfit")
	Integer updateAgentProfit(Map<String, List<AgentProfitVo>> map);

	@SelectProvider(type=com.midai.pay.agent.provider.AgentProfitStatisticsProvider.class, method="findListByParentAgent")
	List<AgentProfitVo> findListByParentAgent(AgentProfitVo pv);

	@SelectProvider(type=com.midai.pay.agent.provider.AgentProfitStatisticsProvider.class, method="queryAgentProfitStatisticsInfo")
	AgentProfitStatisticsVo queryAgentProfitStatisticsInfo(AgentProfitQuery query);

	@SelectProvider(type=com.midai.pay.agent.provider.AgentProfitStatisticsProvider.class, method="queryListByQuery")
	List<AgentProfitVo> queryListByQuery(AgentProfitQuery query);

//	@Select(" select")
//	AgentProfitStatisticsVo queryAgentProfitStatisticsInfo(Agent minLg);
	
}
