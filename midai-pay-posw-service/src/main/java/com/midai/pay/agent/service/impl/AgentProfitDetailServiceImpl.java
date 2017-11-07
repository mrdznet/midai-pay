package com.midai.pay.agent.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.query.PageVo;
import com.midai.pay.agent.entity.Agent;
import com.midai.pay.agent.mapper.AgentProfitDetailMapper;
import com.midai.pay.agent.query.AgentProfitQuery;
import com.midai.pay.agent.service.AgentProfitDetailService;
import com.midai.pay.agent.service.AgentService;
import com.midai.pay.agent.vo.AgentProfitStatisticsVo;
import com.midai.pay.agent.vo.AgentProfitVo;
import com.midai.pay.user.service.SystemOrganizationService;
import com.midai.pay.user.service.SystemUserService;

@Service
public class AgentProfitDetailServiceImpl implements AgentProfitDetailService {

	@Autowired
	private AgentProfitDetailMapper mapper;

	@Reference
	private AgentService agentService;

	@Reference
	private SystemUserService systemUserService;

	@Reference
	private SystemOrganizationService systemOrganizationService;

	@Override
	public PageVo<AgentProfitVo> paginateAgentProfitDetail(AgentProfitQuery query, String exp) {

		PageVo<AgentProfitVo> pv = new PageVo<AgentProfitVo>();
		String agentNo = query.getAgentId();
		query = getAgentProfitQuery(query);
		
		Agent agent = new Agent();
		agent.setCode(query.getCode());
		agent.setAgentNo(query.getAgentId());
		agent.setName(query.getAgentName());
		
		List<Agent> agentList = agentService.findListByLikeAgent(agent);
		
		StringBuffer codes = new StringBuffer();
		
		if(agentList != null && agentList.size() > 0) {
			
			for(Agent a : agentList) {
				codes.append(a.getCode()).append(",");
			}
		}
		
		if(codes.length() > 0) {
			codes.setLength(codes.length()-1);
		}
		
		query.setAgentId(agentNo);
		query.setCode(codes.toString());
		List<AgentProfitVo> list = mapper.findListByAgentProfitDetailQuery(query);
		pv.setRows(list);
		if (exp == null || !exp.equals("exp")) {
			Integer count = mapper.findCountByAgentProfitDetailQuery(query);
			pv.setTotal(count);
		}
		return pv;
	}

	@Override
	public AgentProfitStatisticsVo queryAgentProfitDetailInfo(AgentProfitQuery query, String exp) {
		// 判断用户是否为系统用户
		Agent agent = agentService.fetchAgentByUserName(query.getUserName());
		if (agent == null) {
			agent = new Agent();
			agent.setAgentNo("AGEi000460");
			agent = agentService.findOne(agent);
		}

		query.setCode(agent.getCode());
		return mapper.queryAgentProfitDetailInfo(query);
	}

	
	private AgentProfitQuery getAgentProfitQuery(AgentProfitQuery query) {
		// 判断用户是否为系统用户
		Agent agent = agentService.fetchAgentByUserName(query.getUserName());
		if (agent == null || StringUtils.isEmpty(agent.getAgentNo())) {
			agent = new Agent();
			agent.setAgentNo("AGEi000460");
			agent = agentService.findOne(agent);
			query.setUserFlag("sys");
		} else {
			query.setUserFlag("agent");
		}


		if(StringUtils.isEmpty(query.getAgentId()) && StringUtils.isEmpty(query.getAgentName())) {
			query.setAgentId(agent.getAgentNo());
		}
		// 设置代理商命名规则
		query.setCode(agent.getCode());
		query.setUserName(agent.getAgentNo());
		return query;
	}
}
