package com.midai.pay.agent.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.framework.common.PinYin;
import com.midai.pay.agent.common.util.AgentConstant;
import com.midai.pay.agent.entity.Agent;
import com.midai.pay.agent.mapper.AgentMapper;
import com.midai.pay.agent.mapper.BoAgentImgMapper;
import com.midai.pay.agent.query.AgentQuery;
import com.midai.pay.agent.service.AgentService;
import com.midai.pay.agent.vo.AgentAllVo;
import com.midai.pay.agent.vo.AgentImgVo;
import com.midai.pay.agent.vo.AgentMerchantRateVo;
import com.midai.pay.agent.vo.AgentNoAndNameVo;
import com.midai.pay.agent.vo.AgentUpdateVo;
import com.midai.pay.agent.vo.AgentVo;
import com.midai.pay.common.po.ReturnVal;
import com.midai.pay.user.entity.SystemOrganizationModel;
import com.midai.pay.user.entity.SystemUser;
import com.midai.pay.user.mapper.SystemOrganizationMapper;
import com.midai.pay.user.mapper.SystemTblSeqMapper;
import com.midai.pay.user.mapper.SystemUserMapper;
import com.midai.pay.user.service.SystemOrgUserForAgentService;
import com.midai.pay.user.service.SystemUserService;
import com.midai.pay.user.vo.SystemOrgUserForAgentVo;

import tk.mybatis.mapper.entity.Example;

@Service
public class AgentServiceImpl extends BaseServiceImpl<Agent> implements AgentService {

	private final AgentMapper mapper;

	private static final String INST_JD_CODE = "301000000001";
	@Autowired
	private SystemOrganizationMapper systemOrganizationMapper;
	@Autowired
	private SystemUserMapper systemUserMapper;
	@Autowired
	private SystemOrgUserForAgentService systemOrgUserForAgentService;
	@Autowired
	private SystemTblSeqMapper systemTblSeqMapper;
	@Autowired
	private BoAgentImgMapper boAgentImgMapper;

	@Autowired
	private SystemUserService systemUserService;
	private static final Logger LOGGER = LoggerFactory.getLogger(AgentServiceImpl.class);

	private static final String TOP_AGENT_PARENT_AGENT_ID = "0";/* 顶级代理父代理编号 */

	public AgentServiceImpl(AgentMapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}

	@Override
	public int queryCount(AgentQuery query) {
		String agentNo = systemUserMapper.findCurrentAgentNo(query.getLoginName());
		query.setANo((null == agentNo) ? "AGEi000460" : agentNo);

		return mapper.queryCount(query);
	}

	@Override
	public List<AgentVo> queryList(AgentQuery query) {
		SystemUser user = systemUserService.loadByUserLoginname(query.getLoginName());
		String agentNo = systemUserMapper.findCurrentAgentNo(query.getLoginName());

		List<AgentVo> agentVoList = new ArrayList<AgentVo>();
		query.setANo((null == agentNo) ? "AGEi000460" : agentNo);
		List<Agent> agentList = mapper.queryList(query);

		Agent at = mapper.getAgentInfo(agentNo);
		if (agentList == null) {
			agentList = new ArrayList<Agent>();
		}
		for (Agent agent : agentList) {
			AgentVo agentVo = new AgentVo();
			if (StringUtils.isNotEmpty(user.getInscode())) {
				if (agent.getName().equals(at.getName())) {
					agentVo.setMarkId(1);
				} else if (agent.getParentAgentName().equals(at.getName())) {
					agentVo.setMarkId(2);
				} else {
					agentVo.setMarkId(3);
				}
			}
			agentVo.setCreateTime(agent.getCreateTime());
			BeanUtils.copyProperties(agent, agentVo);
			agentVoList.add(agentVo);
		}
		return agentVoList;
	}

	@Override
	public int queryNoAndNameCount(AgentQuery query) {
		if (StringUtils.isNotBlank(query.getName())) {
			return mapper.queryNoAndNameCountByName(query);
		} else {
			return mapper.queryNoAndNameCount(query);
		}
	}

	@Override
	public List<AgentNoAndNameVo> queryNoAndNameList(AgentQuery query) {
		List<AgentNoAndNameVo> agtNoAndNameList = new ArrayList<AgentNoAndNameVo>();
		if (StringUtils.isNotBlank(query.getName())) {
			agtNoAndNameList = mapper.queryNoAndNameListByName(query);
		} else {
			agtNoAndNameList = mapper.queryNoAndNameList(query);
		}
		return agtNoAndNameList;
	}

	@Override
	public int queryNoAndNameTargetCount(AgentQuery query) {
		return mapper.queryNoAndNameTargetCount(query);
	}

	@Override
	public List<AgentNoAndNameVo> queryNoAndNameTargetList(AgentQuery query) {
		List<AgentNoAndNameVo> agtNoAndNameList = mapper.queryNoAndNameTargetList(query);
		if (agtNoAndNameList == null) {
			agtNoAndNameList = new ArrayList<AgentNoAndNameVo>();
		}
		return agtNoAndNameList;
	}

	@Override
	public int queryNoAndNameTopCount(AgentQuery query) {
		if (StringUtils.isNotBlank(query.getName())) {
			return mapper.queryNoAndNameCountByName(query);
		} else {
			return mapper.queryNoAndNameCount(query);
		}
	}

	@Override
	public Map<String, Object> queryChildAgentList(AgentQuery query) {
		List<Agent> list = getChildAgentList(query);
		List<AgentNoAndNameVo> annlist = new ArrayList<AgentNoAndNameVo>();
		for (Agent a : list) {
			AgentNoAndNameVo ann = new AgentNoAndNameVo();
			ann.setAgentNo(a.getAgentNo());
			ann.setName(a.getName());
			annlist.add(ann);
		}
		query.setLimit(5);
		int startN = (query.getPageNumber() - 1) * query.getPageSize();
		int end = startN + query.getPageSize();
		if (startN >= annlist.size()) {
			startN = annlist.size();
		}
		if (end >= annlist.size()) {
			end = annlist.size();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<AgentNoAndNameVo> reslist = new ArrayList<AgentNoAndNameVo>();

		for (int i = startN; i < end; i++) {
			reslist.add(annlist.get(i));
		}
		map.put("total", annlist.size());
		map.put("rows", reslist);
		return map;
	}

	@Override
	public List<AgentNoAndNameVo> queryNoAndNameTopList(AgentQuery query) {
		List<AgentNoAndNameVo> agtNoAndNameList = new ArrayList<AgentNoAndNameVo>();
		if (StringUtils.isNotBlank(query.getName())) {
			agtNoAndNameList = mapper.queryNoAndNameListByName(query);
		} else {
			agtNoAndNameList = mapper.queryNoAndNameList(query);
		}
		if (agtNoAndNameList != null && !agtNoAndNameList.isEmpty()) {
			List<Agent> agentList = mapper.getAgentList();
			for (AgentNoAndNameVo vo : agtNoAndNameList) {
				Agent topAgent = getTopAgentByAgentNo(vo.getAgentNo(), agentList);
				if (topAgent != null && StringUtils.isNotBlank(topAgent.getAgentNo())) {
					vo.setTopAgentId(topAgent.getAgentNo());
					vo.setTopAgentName(topAgent.getName());
				} else {
					vo.setTopAgentId(vo.getAgentNo());
					vo.setTopAgentName(vo.getName());
				}
			}
		}
		return agtNoAndNameList;
	}

	@Override
	public AgentVo getInsertInfo(String currUser) {
		AgentVo vo = new AgentVo();
		Agent a = this.fetchAgentByUserName(currUser);
		// a = this.findOne(a);
		// if(a == null) {
		// a = new Agent();
		// a.setAgentNo("AGEi000460");
		// a = mapper.selectOne(a);
		// }

		if (a != null) {
			BeanUtils.copyProperties(a, vo);
		}

		return vo;
	}

	@Override
	@Transactional(propagation = Propagation.NESTED)
	public ReturnVal<String> insertAgent(AgentVo agentVo, String userName, String password, List<AgentImgVo> imgList) {
		Agent agent = new Agent();
		// vo和model数据转换
		BeanUtils.copyProperties(agentVo, agent);

		// 验证代理商名称
		Example exampleName = new Example(Agent.class);
		exampleName.createCriteria().andEqualTo("name", agent.getName());
		int count = mapper.selectCountByExample(exampleName);
		if (count > 0) {
			LOGGER.info(" 该代理商名称已经注册！");
			throw new RuntimeException(" 该代理商名称已经注册！");
		}
		// 验证代理商账号
		Example exampleShortName = new Example(Agent.class);
		exampleShortName.createCriteria().andEqualTo("shortName", agent.getShortName());
		count = mapper.selectCountByExample(exampleShortName);
		if (count > 0) {
			LOGGER.info("该登录账号已经注册！");
			throw new RuntimeException("该登录账号已经注册！");
		}
		// 验证代理商手机号码
		Example exampleMobile = new Example(Agent.class);
		exampleMobile.createCriteria().andEqualTo("mobile", agent.getMobile());
		count = mapper.selectCountByExample(exampleMobile);
		if (count > 0) {
			LOGGER.info("该手机号已经注册！");
			throw new RuntimeException("该手机号已经注册！");
		}

		/*********************** 新增代理商 ************************/
		// 获取顶级代理商简称拼音首字母(大写)
		List<Agent> agentList = mapper.getAgentList();
		Agent topAgent = getTopAgentByAgentNo(agent.getParentAgentId(), agentList);
		String shortName = "";
		if (topAgent != null && StringUtils.isNotBlank(topAgent.getShortName())) {
			shortName = topAgent.getShortName();
		} else {
			shortName = agent.getShortName();
		}
		String topPinyin = "";
		if (StringUtils.isNotBlank(shortName)) {
			topPinyin = StringUtils.upperCase(PinYin.getPinYinHeadChar(shortName));
		}
		// 获取父级代理商简称拼音首字母(大写)和等级
		String parentPinyin = "";
		Agent parentAgent = mapper.getShortNameAndLevel(agent.getParentAgentId());
		if (parentAgent != null && StringUtils.isNotBlank(parentAgent.getShortName())) {
			parentPinyin = StringUtils.upperCase(PinYin.getPinYinHeadChar(parentAgent.getShortName()));
		}
		// 获取当前登录用户姓名拼音首字母大写
		Example exampleUser = new Example(SystemUser.class);
		exampleUser.createCriteria().andEqualTo("loginname", userName);
		List<SystemUser> systemUserList = systemUserMapper.selectByExample(exampleUser);
		SystemUser systemUser = systemUserList.get(0);
		String loginPinyin = StringUtils.upperCase(PinYin.getPinYinHeadChar(systemUser.getUsername()));

		// 代理商编码规则：顶级代理商简称拼音首字母(大写)-父级代理商简称拼音首字母(大写)-当前登录用户名大写-yyMMddxxx,如：MF-FJ-WX-160924001
		String dateFormat = new SimpleDateFormat("yyMMdd").format(new Date());
		StringBuilder queryParam = new StringBuilder(topPinyin).append("-").append(parentPinyin).append("-")
				.append(loginPinyin).append("-").append(dateFormat);
		// 获取数据库当天最新代理商编码
		String latestAgentNo = mapper.getLastestAgentNo(queryParam.toString());
		String newSeqNo = "";
		if (StringUtils.isBlank(latestAgentNo)) {
			newSeqNo = "000";
		} else {
			String latestSeqNo = StringUtils.substring(latestAgentNo, latestAgentNo.length() - 3,
					latestAgentNo.length());
			newSeqNo = String.format("%03d", Integer.parseInt(latestSeqNo) + 1);
		}

		String agentNo = queryParam + newSeqNo;
		agent.setAgentNo(agentNo);
		agent.setOrgCode(agentNo);

		// 获取父代理商inscode
		String inscode = mapper.findInscode(agent.getParentAgentId());
		agent.setInscode(inscode);

		// 序列
		String code = mapper.findCode(agent.getParentAgentId());
		int currVal = systemTblSeqMapper.findAgentValue();
		agent.setCode(code + "-" + currVal);

		agent.setNonCardDebitRate((null == agent.getNonCardCreditRate()) ? null : agent.getNonCardCreditRate());
		agent.setAgentLevle(parentAgent.getAgentLevle() + 1);
		agent.setOpState(AgentConstant.OP_STATE_OPEN);
		agent.setCreateUser(userName);
		agent.setCreateTime(new Date());
		agent.setUpdateUser(userName);
		mapper.insertSelective(agent);

		/*********************** 新增代理商组织机构和登录用户 *********************/
		Example exampleOrg = new Example(SystemOrganizationModel.class);
		exampleOrg.createCriteria().andEqualTo("agentNum", agent.getParentAgentId());
		List<SystemOrganizationModel> sysOrgModelList = systemOrganizationMapper.selectByExample(exampleOrg);
		SystemOrgUserForAgentVo vo = new SystemOrgUserForAgentVo();
		if (sysOrgModelList != null && !sysOrgModelList.isEmpty()) {
			SystemOrganizationModel parentModel = sysOrgModelList.get(0);
			vo.setLevel(parentModel.getLevel() + 1);
			vo.setParentId(parentModel.getOrganizationId());
		}
		vo.setOrganizationName(agent.getShortName());
		vo.setAgentNum(agent.getAgentNo());
		vo.setLoginname(agent.getShortName());
		vo.setPassword(password);
		vo.setUsername(agent.getName());
		vo.setMobile(agent.getMobile());
		vo.setInscode(inscode);
		systemOrgUserForAgentService.insertSystemOrgUserByAgent(vo);

		/*********************** 保存代理商图片 *********************/
		if (null != imgList && imgList.size() > 0) {
			for (AgentImgVo img : imgList) {
				img.setAgentNo(agentNo);
				boAgentImgMapper.insertImg(img);
			}
		}

		return ReturnVal.SUCCESS();
	}

	@Override
	public AgentAllVo getUpdatInfo(Integer id) {
		AgentAllVo vo = new AgentAllVo();
		if (id == null) {
			LOGGER.info("代理商id为空！");
			throw new RuntimeException("代理商id为空！");
		}
		Agent agent = mapper.selectByPrimaryKey(id);
		if (agent == null) {
			LOGGER.info("代理商信息为空！");
			throw new RuntimeException("代理商信息为空！");
		}
		// 代理商基本信息
		AgentVo agentVo = new AgentVo();
		BeanUtils.copyProperties(agent, agentVo);
		
		agentVo.setShortName(systemUserMapper.getLoginNameByMobile(agentVo.getMobile().trim()));
		vo.setAgentVo(agentVo);

		// 图片信息
		vo.setImgVo(boAgentImgMapper.findByAgentNo(agent.getAgentNo()));

		return vo;
	}

	@Override
	@Transactional
	public ReturnVal<String> updateAgent(AgentUpdateVo vo, String userName) {
		AgentVo agentVo = vo.getAgentVo();
		if (agentVo.getId() == null) {
			LOGGER.info("代理商id为空！");
			throw new RuntimeException("代理商id为空！");
		}

		// 验证代理商名称
		Example exampleName = new Example(Agent.class);
		exampleName.createCriteria().andEqualTo("name", agentVo.getName()).andNotEqualTo("id", agentVo.getId());
		int count = mapper.selectCountByExample(exampleName);
		if (count > 0) {
			LOGGER.info("该代理商名称已经注册！");
			throw new RuntimeException("该代理商名称已经注册！");
		}

		// 验证代理商登录账号
		Example exampleShortName = new Example(Agent.class);
		exampleShortName.createCriteria().andEqualTo("shortName", agentVo.getShortName()).andNotEqualTo("id",
				agentVo.getId());
		count = mapper.selectCountByExample(exampleShortName);
		if (count > 0) {
			LOGGER.info("该代理商简称已经注册！");
			throw new RuntimeException("该代理商简称已经注册！");
		}
		// 验证代理商手机号码
		Example exampleMobile = new Example(Agent.class);
		exampleMobile.createCriteria().andEqualTo("mobile", agentVo.getMobile()).andNotEqualTo("id", agentVo.getId());
		count = mapper.selectCountByExample(exampleMobile);
		if (count > 0) {
			LOGGER.info("该手机号已经注册！");
			throw new RuntimeException("该手机号已经注册！");
		}

		/********************** 更新代理商 **************************/
		Agent oldAgent = mapper.selectByPrimaryKey(agentVo.getId());
		if (oldAgent == null) {
			LOGGER.info("代理商信息为空！");
			throw new RuntimeException("代理商信息为空！");
		}
		agentVo.setCreateTime(oldAgent.getCreateTime());

		agentVo.setNonCardDebitRate((null == agentVo.getNonCardCreditRate()) ? null : agentVo.getNonCardCreditRate());
		agentVo.setUpdateUser(userName);

		BeanUtils.copyProperties(agentVo, oldAgent);
		mapper.updateByPrimaryKey(oldAgent);

		/************************** 批量更新商户签约扣率 *************************/
		/*
		 * List<AgentMerchantRateVo> agtMerRateVoList =
		 * vo.getAgtMerRateVoList(); for(AgentMerchantRateVo amvo
		 * :agtMerRateVoList){ if(vo.getAgentVo().getSingleMoney() !=
		 * amvo.getSingleMoney()){ if(agtMerRateVoList != null &&
		 * !agtMerRateVoList.isEmpty()) { Map<String, List<AgentMerchantRateVo>>
		 * map = new HashMap<String, List<AgentMerchantRateVo>>();
		 * map.put("list", agtMerRateVoList); mapper.updateBatchMerRate(map); }
		 * } }
		 */
		/************************** 更新报备翰银商户扣率 *************************/
		// TODO 移到商户列表更新
		/*
		 * for(AgentMerchantRateVo amrv : agtMerRateVoList) { String mercNo =
		 * amrv.getMercNo(); BoCustomer customer =
		 * boCustomerService.findByPrimaryKey(mercNo); String mercId =
		 * boCustomerService.getInstMercId(customer.getMercId(), INST_JD_CODE);
		 * if(StringUtils.isNotEmpty(mercId)) {
		 * boCustomerService.updatePrepareInformation(customer, mercId); } }
		 */

		// 更新代理商对应组织机构的名称
		systemOrganizationMapper.updateOrgNameByAgtNo(oldAgent.getShortName(), oldAgent.getAgentNo());
		// 更新用户表里面的手机号
		systemUserMapper.updateMobilebyLoginname(vo.getAgentVo().getMobile(), vo.getOldMobile());

		// 代理商图片信息
		boAgentImgMapper.deleteImgByAgentNo(oldAgent.getAgentNo());
		List<AgentImgVo> imgList = vo.getImgVo();
		if (null != imgList && imgList.size() > 0) {
			for (AgentImgVo img : imgList) {
				img.setAgentNo(oldAgent.getAgentNo());
				boAgentImgMapper.insertImg(img);
			}
		}

		return ReturnVal.SUCCESS();
	}

	@Override
	public int queryAgtMerRateCount(AgentQuery query) {
		return mapper.queryAgtMerRateCount(query);
	}

	@Override
	public List<AgentMerchantRateVo> queryAgtMerRateList(AgentQuery query) {
		List<AgentMerchantRateVo> agtMerRateList = mapper.queryAgtMerRateList(query);
		if (agtMerRateList == null) {
			agtMerRateList = new ArrayList<AgentMerchantRateVo>();
		} else {
			// 应前端需要添加，无业务含义
			int i = 0;
			for (AgentMerchantRateVo vo : agtMerRateList) {
				vo.setId(i++);
			}
		}
		return agtMerRateList;
	}

	@Override
	@Transactional
	public ReturnVal<String> updateBatchAgentRate(List<AgentVo> agentVoList, String userName) {
		if (agentVoList == null || agentVoList.isEmpty()) {
			LOGGER.info("批量修改扣率信息为空！");
			throw new RuntimeException("{\"errorMsg\":\"批量修改扣率信息为空！\"}");
		}
		List<Agent> agentList = new ArrayList<Agent>();
		for (AgentVo agentVo : agentVoList) {
			Agent agent = new Agent();
			BeanUtils.copyProperties(agentVo, agent);
			agent.setUpdateUser(userName);
			agentList.add(agent);
		}
		if (agentList != null && !agentList.isEmpty()) {
			Map<String, List<Agent>> map = new HashMap<String, List<Agent>>();
			map.put("list", agentList);
			mapper.updateBatchAgentRate(map);
		}
		return ReturnVal.SUCCESS();
	}

	@Override
	public ReturnVal<String> updateRate(AgentVo agentVo) {
		mapper.updateRate(agentVo);

		return ReturnVal.SUCCESS();
	}

	@Override
	public List<AgentVo> excelExport(AgentQuery query) {
		List<AgentVo> agentVoList = new ArrayList<AgentVo>();
		String agentNo = systemUserMapper.findCurrentAgentNo(query.getLoginName());
		query.setANo((null == agentNo) ? "AGEi000460" : agentNo);

		List<Agent> agentList = mapper.excelExport(query);
		if (agentList == null) {
			agentList = new ArrayList<Agent>();
		}
		for (Agent agent : agentList) {
			AgentVo agentVo = new AgentVo();
			BeanUtils.copyProperties(agent, agentVo);
			agentVoList.add(agentVo);
		}
		return agentVoList;
	}

	@Override
	public List<AgentVo> excelExportChecked(Integer[] ids) {
		List<AgentVo> agentVoList = new ArrayList<AgentVo>();
		StringBuilder idStr = new StringBuilder();
		for (Integer id : ids) {
			idStr.append("'").append(id).append("',");
		}
		idStr.setLength(idStr.length() - 1);
		List<Agent> agentList = mapper.fetchExcelExport(idStr.toString());
		if (agentList == null) {
			agentList = new ArrayList<Agent>();
		}
		for (Agent agent : agentList) {
			AgentVo agentVo = new AgentVo();
			BeanUtils.copyProperties(agent, agentVo);
			agentVoList.add(agentVo);
		}
		return agentVoList;
	}

	@Override
	public Agent getTopAgentByAgentNo(String agentNo, List<Agent> agentList) {
		Agent agent = new Agent();
		if (StringUtils.isNotBlank(agentNo)) {
			agent = getTopAgent(agentNo, agentList);
			return agent;
		}
		return agent;
	}

	private Agent getTopAgent(String agentNo, List<Agent> agentList) {
		Agent topAgent = new Agent();
		if (agentList != null && !agentList.isEmpty()) {
			for (Agent agent : agentList) {
				if (StringUtils.equals(agentNo, agent.getAgentNo())) {
					if (agent.getAgentLevle() == 1) {
						return agent;
					} else {
						topAgent = getTopAgent(agent.getParentAgentId(), agentList);
					}
				}
			}
		}
		return topAgent;
	}

	@Override
	public List<String> fetchAllParentAgentNo(String agentNo) {
		List<Agent> agentList = mapper.selectAll();
		return getParentAgentNo(agentNo, agentList);
	}

	@Override
	public Agent fetchAgentByUserName(String userName) {
		String agentNo = systemOrgUserForAgentService.getAgentCodeByLoginname(userName);
		Agent agent = new Agent();
		agent.setAgentNo(agentNo);
		if (StringUtils.isNotEmpty(agentNo)) {
			agent = mapper.selectOne(agent);
		}
		return agent;
	}

	@Override
	public List<Agent> findAgentsByUserName(String userName) {
		List<Agent> agentList = null;
		String agentNo = systemOrgUserForAgentService.getAgentCodeByLoginname(userName);

		if (StringUtils.isNotEmpty(agentNo)) { // 单个代理商
			agentList = new ArrayList<Agent>();

			Agent agent = mapper.getAgentInfo(agentNo);
			agentList.add(agent);

		} else {
			String inscode = systemUserMapper.getInscode(userName);
			if (StringUtils.isNotBlank(inscode)) { // 运营

				agentList = mapper.findAgentInfoByInscode(inscode);
			} else { // 管理员

				agentList = mapper.getAgentList();
			}
		}

		return agentList;
	}

	private List<String> getParentAgentNo(String agentNo, List<Agent> agentList) {
		List<String> agentNoList = new ArrayList<String>();
		for (Agent agent : agentList) {
			if (!StringUtils.equals(agentNo, agent.getAgentNo())) {
				continue;
			}
			if (StringUtils.equals(agent.getParentAgentId(), TOP_AGENT_PARENT_AGENT_ID)) {
				return agentNoList;
			} else {
				agentNoList.add(agent.getParentAgentId());
				agentNoList.addAll(getParentAgentNo(agent.getParentAgentId(), agentList));
				break;
			}
		}
		return agentNoList;
	}

	@Override
	public String getAgentNoParam(String agentNo) {
		AgentQuery query = new AgentQuery();
		query.setAgentNor(agentNo);
		List<Agent> childsAgentNoList = getChildAgentList(query);
		StringBuilder agtNo = new StringBuilder("'" + agentNo + "'");
		for (Agent childAgent : childsAgentNoList) {
			agtNo.append(",'").append(childAgent.getAgentNo()).append("'");
		}
		return agtNo.toString();
	}

	@Override
	public List<Agent> getChildAgentList(AgentQuery query) {
		List<Agent> allAgentNoList = new ArrayList<Agent>();

		List<Agent> returnList = new ArrayList<Agent>();
		allAgentNoList = mapper.selectAll();

		if (allAgentNoList != null && allAgentNoList.size() > 0) {

			if (StringUtils.isNotEmpty(query.getAgentNor())) {
				List<Agent> childsAgentNoList = new ArrayList<Agent>();
				getChildsAgentNo(allAgentNoList, childsAgentNoList, query.getAgentNor());
				allAgentNoList = childsAgentNoList;
			}

			if (StringUtils.isNotEmpty(query.getAgentNo()) || StringUtils.isNotEmpty(query.getName())) {
				for (Agent agent : allAgentNoList) {
					if (StringUtils.isNotEmpty(query.getAgentNo()) && StringUtils.isNotEmpty(query.getName())) {
						if (agent.getAgentNo().contains(query.getAgentNo())
								&& agent.getName().contains(query.getName())) {
							returnList.add(agent);
						}
					} else if (StringUtils.isNotEmpty(query.getAgentNo()) && StringUtils.isEmpty(query.getName())) {
						if (agent.getAgentNo().contains(query.getAgentNo())) {
							returnList.add(agent);
						}
					} else if (StringUtils.isEmpty(query.getAgentNo()) && StringUtils.isNotEmpty(query.getName())) {
						if (agent.getName().contains(query.getName())) {
							returnList.add(agent);
						}
					}
				}
				
			} else {
				returnList = allAgentNoList;
			}

		} 
		return returnList;
	}

	/**
	 * 递归获取所有的子代理商编号
	 * 
	 * @param allAgentNoList
	 * @param childsAgentNoList
	 * @param agentNo
	 */
	private void getChildsAgentNo(List<Agent> allAgentNoList, List<Agent> childsAgentNoList, String agentNo) {
		if (allAgentNoList != null && !allAgentNoList.isEmpty() && StringUtils.isNotBlank(agentNo)) {
			for (Agent agent : allAgentNoList) {
				if (StringUtils.equals(agent.getParentAgentId(), agentNo)) {
					childsAgentNoList.add(agent);
					getChildsAgentNo(allAgentNoList, childsAgentNoList, agent.getAgentNo());
				}
			}
		}
	}

	public List<Agent> getByAgentDeviceDeviceId(String deviceId) {
		return mapper.getByAgentDeviceDeviceId(deviceId);
	}

	@Override
	public List<Agent> getByDeviceNo(String deviceid) {
		return mapper.getByDeviceNo(deviceid);
	}

	@Override
	public Agent getTopAgentByNameAndParentId(String haibeiOrg, int i) {
		Example example = new Example(Agent.class);
		example.createCriteria().andEqualTo("parentAgentId", i).andEqualTo("inscode", haibeiOrg);
		List<Agent> list = mapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public String findAllAgentNoByInscode(String inscode) {
		List<String> agentNos = mapper.findAllAgentNoByInscode(inscode);
		StringBuffer agentNostr = new StringBuffer();
		for (String agentNo : agentNos) {
			agentNostr.append("'").append(agentNo).append("',");
		}
		agentNostr.setLength(agentNostr.length() - 1);
		return agentNostr.toString();
	}

	@Override
	public List<Agent> findListByLikeAgent(Agent searchAgent) {

		return mapper.findListByLikeAgent(searchAgent);
	}

	@Override
	public List<Agent> selectAgentListByCodes(String codes) {

		return mapper.findAgentListByCodes(codes);
	}

	@Override
	public AgentVo getAgentInfo(String agentNo) {
		AgentVo vo = new AgentVo();

		Agent at = mapper.getAgentInfo(agentNo);

		if (null != at) {
			BeanUtils.copyProperties(at, vo);
		}

		return vo;
	}

}
