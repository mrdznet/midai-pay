package com.midai.pay.agent.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.query.PageVo;
import com.midai.pay.agent.common.enums.AgentProfitCustomerGradeEnum;
import com.midai.pay.agent.entity.Agent;
import com.midai.pay.agent.mapper.AgentProfitStatisticsMapper;
import com.midai.pay.agent.query.AgentProfitQuery;
import com.midai.pay.agent.service.AgentProfitService;
import com.midai.pay.agent.service.AgentProfitStatisticsService;
import com.midai.pay.agent.service.AgentService;
import com.midai.pay.agent.vo.AgentProfitStatisticsVo;
import com.midai.pay.agent.vo.AgentProfitVo;
import com.midai.pay.user.service.SystemOrganizationService;
import com.midai.pay.user.service.SystemUserService;

@Service
public class AgentProfitStatisticsServiceImpl implements AgentProfitStatisticsService {

	@Autowired
	private AgentProfitStatisticsMapper mapper;

	@Reference
	private AgentProfitService agentProfitService;

	@Reference
	private AgentService agentService;

	@Reference
	private SystemUserService systemUserService;

	@Reference
	private SystemOrganizationService systemOrganizationService;

	@Override
	public PageVo<AgentProfitVo> paginateAgentProfit(AgentProfitQuery query, boolean flag) {
		PageVo<AgentProfitVo> pv = new PageVo<AgentProfitVo>();
		// 转换查询条件
		query = getAgentProfitQuery(query);
		// 查询所有满足条件的数据
		List<AgentProfitVo> apList = mapper.findListByAgentProfitQuery(query);
		pv.setRows(apList);
		if(flag) {
			Integer count = mapper.findCountByAgentProfitQuery(query);
			pv.setTotal(count);
		}
		return pv;
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

		if (StringUtils.isEmpty(query.getAgentId()) && StringUtils.isEmpty(query.getAgentName())) {
			query.setAgentId(agent.getAgentNo());
		}
		// 设置代理商命名规则
		query.setCode(agent.getCode());
		query.setUserName(agent.getAgentNo());
		return query;
	}

	@Override
	public List<AgentProfitVo> findListByParentAgentId(AgentProfitVo pv) {
		Agent agent = new Agent();
		agent.setAgentNo(pv.getAgentId());
		agent = agentService.findOne(agent);
		pv.setCode(agent.getCode());
		List<AgentProfitVo> list = mapper.findListByParentAgent(pv);
		return list;
	}

	@Override
	public Integer updateAgentProfit(List<AgentProfitVo> voList) {
		Map<String, List<AgentProfitVo>> map = new HashMap<String, List<AgentProfitVo>>();
		map.put("list", voList);
		return mapper.updateAgentProfit(map);
	}

	@Override
	public List<AgentProfitVo> queryListByQuery(AgentProfitQuery query) {
		// 转换查询条件
		query = getAgentProfitQuery(query);

		Agent agent = new Agent();
		agent.setCode(query.getCode());
		agent.setAgentNo(query.getAgentId());
		agent.setName(query.getAgentName());

		List<Agent> agentList = agentService.findListByLikeAgent(agent);

		StringBuffer codes = new StringBuffer();

		if (agentList != null && agentList.size() > 0) {

			for (Agent a : agentList) {
				if (query.getFlag().equals(AgentProfitCustomerGradeEnum.GRADE_BJ.getCode())) {
					codes.append(a.getCode()).append(",");
				} else if (query.getFlag().equals(AgentProfitCustomerGradeEnum.GRADE_XJ.getCode())) {
					codes.append(a.getCode() + "-").append(",");
				}
			}
		}

		if (codes.length() > 0) {
			codes.setLength(codes.length() - 1);
		}

		query.setCode(codes.toString());
		return mapper.queryListByQuery(query);
	}

	@Override
	public AgentProfitStatisticsVo queryAgentProfitStatisticsInfo(AgentProfitQuery query) {
		// 转换查询条件
		query = getAgentProfitQuery(query);

		Agent agent = new Agent();
		agent.setCode(query.getCode());
		agent.setAgentNo(query.getAgentId());
		agent.setName(query.getAgentName());

		List<Agent> agentList = agentService.findListByLikeAgent(agent);
		Agent minLg = new Agent();
		minLg.setCode("");
		for(Agent a : agentList) {
			if(a.getCode().length() < minLg.getCode().length()) {
				BeanUtils.copyProperties(a, minLg);
			}
		}
//		return mapper.queryAgentProfitStatisticsInfo(minLg);
		return null;
	}

}
