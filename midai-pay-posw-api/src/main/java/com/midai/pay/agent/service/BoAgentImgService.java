package com.midai.pay.agent.service;

import com.midai.framework.common.BaseService;
import com.midai.pay.agent.entity.AgentImg;

public interface BoAgentImgService extends BaseService<AgentImg> {

	AgentImg getByAgentNoAndType(String agentNo, Integer id);
	
}
