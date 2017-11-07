package com.midai.pay.agent.service;

import com.midai.framework.common.BaseService;
import com.midai.pay.agent.entity.AgentTemplet;
import com.midai.pay.agent.vo.AgentTempletVo;
import com.midai.pay.agent.vo.CustomerTempletVo;

public interface AgentTempletService extends BaseService<AgentTemplet> {
	
	boolean updateAgentTemplet(AgentTempletVo agentTempletVo);
	
	AgentTempletVo getAgentTemplet(String loginUser);
	
	
	
	boolean updateCustomerTemplet(CustomerTempletVo CustomerTempletVo);
	
	CustomerTempletVo getCustomerTemplet(String loginUser);

	AgentTempletVo getAgentTempletByAgentNo(String agentNo);

	CustomerTempletVo getCustomerTempletByAgentNo(String agentNo);
	
}
