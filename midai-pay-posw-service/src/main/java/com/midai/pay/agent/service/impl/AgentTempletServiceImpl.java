package com.midai.pay.agent.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.agent.entity.Agent;
import com.midai.pay.agent.entity.AgentTemplet;
import com.midai.pay.agent.mapper.AgentTempletMapper;
import com.midai.pay.agent.service.AgentService;
import com.midai.pay.agent.service.AgentTempletService;
import com.midai.pay.agent.vo.AgentTempletVo;
import com.midai.pay.agent.vo.AgentVo;
import com.midai.pay.agent.vo.CustomerTempletVo;

import tk.mybatis.mapper.entity.Example;

@Service
public class AgentTempletServiceImpl extends BaseServiceImpl<AgentTemplet> implements AgentTempletService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AgentTempletServiceImpl.class);

	private static final int CUSTOMER_TEMPLE = 1;
	private static final int AGENT_TEMPLE = 2;
	
	private final AgentTempletMapper templetMapper;

	@Autowired
	private AgentService agentService;
	
	public AgentTempletServiceImpl(AgentTempletMapper templetMapper) {
		super(templetMapper);
		this.templetMapper = templetMapper;
	}

	@Override
	public boolean updateAgentTemplet(AgentTempletVo vo) {
		AgentTemplet templet = new AgentTemplet();
		templet.setType(AGENT_TEMPLE);
		templet.setNonCardCreditRate(vo.getNonCardRate());
		templet.setNonCardDebitRate(vo.getNonCardRate());
		BeanUtils.copyProperties(vo, templet);
		
		Example ex = new Example(AgentTemplet.class);
		ex.createCriteria().andEqualTo("type", AGENT_TEMPLE).andEqualTo("agentNo", vo.getAgentNo());
		List<AgentTemplet> list = templetMapper.selectByExample(ex);
		
		int i=0;
		if(null != list && list.size() > 0){
			i = templetMapper.updateAgentTemplet(templet);
		}else{
			i = templetMapper.insertSelective(templet);
		}
		
		return i > 0;
	}

	
	@Override
	public AgentTempletVo getAgentTemplet(String loginUser) {
		AgentTempletVo vo = new AgentTempletVo();
		
		Agent agent = agentService.fetchAgentByUserName(loginUser);
		if(null != agent && !StringUtils.isEmpty(agent.getAgentNo())){
			Example ex = new Example(AgentTemplet.class);
			ex.createCriteria().andEqualTo("type", AGENT_TEMPLE).andEqualTo("agentNo", agent.getAgentNo());
			List<AgentTemplet> list = templetMapper.selectByExample(ex);
			
			if(null != list && list.size() > 0){// 已保存模板
				AgentTemplet temple = list.get(0);
				
				BeanUtils.copyProperties(temple, vo);
				vo.setNonCardRate(temple.getNonCardCreditRate());
			}else{// 未保存模板
				if(null != agent){
					BeanUtils.copyProperties(agent, vo);
					vo.setNonCardRate(agent.getNonCardCreditRate());
				}
			}
		}
		
		return vo;
	}

	
	@Override
	public boolean updateCustomerTemplet(CustomerTempletVo vo) {
		AgentTemplet templet = new AgentTemplet();
		templet.setType(CUSTOMER_TEMPLE);
		templet.setNonCardCreditRate(vo.getNonCardRate());
		templet.setNonCardDebitRate(vo.getNonCardRate());
		BeanUtils.copyProperties(vo, templet);
		
		Example ex = new Example(AgentTemplet.class);
		ex.createCriteria().andEqualTo("type", CUSTOMER_TEMPLE).andEqualTo("agentNo", vo.getAgentNo());
		List<AgentTemplet> list = templetMapper.selectByExample(ex);
		
		int i = 0;
		if(null != list && list.size() > 0){
			i = templetMapper.updateCustomerTemplet(templet);
		}else{
			i = templetMapper.insertSelective(templet);
		}
		
		return i > 0;
	}


	@Override
	public CustomerTempletVo getCustomerTemplet(String loginUser) {
		CustomerTempletVo vo = new CustomerTempletVo();
		
		Agent agent = agentService.fetchAgentByUserName(loginUser);
		if(null != agent && !StringUtils.isEmpty(agent.getAgentNo())){
			Example ex = new Example(AgentTemplet.class);
			ex.createCriteria().andEqualTo("type", CUSTOMER_TEMPLE).andEqualTo("agentNo", agent.getAgentNo());
			List<AgentTemplet> list = templetMapper.selectByExample(ex);
			
			if(null != list && list.size() > 0){ // 已保存模板
				AgentTemplet temple = list.get(0);
				AgentVo agentInfo = agentService.getAgentInfo(temple.getAgentNo());
				BeanUtils.copyProperties(temple, vo);
				vo.setName(agentInfo.getName());
				vo.setNonCardRate(temple.getNonCardCreditRate());
			}else{ // 未保存模板
				if(null != agent){
					BeanUtils.copyProperties(agent, vo);
					vo.setNonCardRate(agent.getNonCardCreditRate());
				}
			}
		}
		
		return vo;
	}

	@Override
	public AgentTempletVo getAgentTempletByAgentNo(String agentNo) {
		
		AgentTempletVo vo = new AgentTempletVo();
		
		if (!StringUtils.isEmpty(agentNo)) {
			AgentVo agentVo = agentService.getAgentInfo(agentNo);
			Example ex = new Example(AgentTemplet.class);
			ex.createCriteria().andEqualTo("type", AGENT_TEMPLE).andEqualTo("agentNo", agentNo);
			List<AgentTemplet> list = templetMapper.selectByExample(ex);
			
			if(null != list && list.size() > 0){ // 已保存模板
				AgentTemplet temple = list.get(0);
				
				BeanUtils.copyProperties(temple, vo);
				vo.setNonCardRate(temple.getNonCardCreditRate());
			}else{// 未保存模板
				if(null != agentVo){
					BeanUtils.copyProperties(agentVo, vo);
					vo.setNonCardRate(agentVo.getNonCardCreditRate());
				}
			}
			
		}
		return vo;
	}
	
	@Override
	public CustomerTempletVo getCustomerTempletByAgentNo(String agentNo) {
		
		CustomerTempletVo vo = new CustomerTempletVo();
		
		if (!StringUtils.isEmpty(agentNo)) {
			AgentVo agentVo = agentService.getAgentInfo(agentNo);
			Example ex = new Example(AgentTemplet.class);
			ex.createCriteria().andEqualTo("type", CUSTOMER_TEMPLE).andEqualTo("agentNo", agentNo);
			List<AgentTemplet> list = templetMapper.selectByExample(ex);
			
			if(null != list && list.size() > 0){ // 已保存模板
				AgentTemplet temple = list.get(0);
				
				BeanUtils.copyProperties(temple, vo);
				vo.setNonCardRate(temple.getNonCardCreditRate());
			}else{// 未保存模板
				if(null != agentVo){
					BeanUtils.copyProperties(agentVo, vo);
					vo.setNonCardRate(agentVo.getNonCardCreditRate());
				}
			}
		}
		return vo;
	}

}
