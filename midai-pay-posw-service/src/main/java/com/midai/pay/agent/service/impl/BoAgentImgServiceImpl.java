package com.midai.pay.agent.service.impl;

import java.util.List;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.agent.entity.AgentImg;
import com.midai.pay.agent.mapper.BoAgentImgMapper;
import com.midai.pay.agent.service.BoAgentImgService;

import tk.mybatis.mapper.entity.Example;

@Service
public class BoAgentImgServiceImpl extends BaseServiceImpl<AgentImg> implements BoAgentImgService {

	private final BoAgentImgMapper mapper;
	
	public BoAgentImgServiceImpl(BoAgentImgMapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}

	@Override
	public AgentImg getByAgentNoAndType(String agentNo, Integer id) {
		Example example= new Example(AgentImg.class);
		List<AgentImg> list = mapper.selectByExample(example);
		if(list != null && list.size() > 0) {
			list.get(0);
		}
		return null;
	}

}
