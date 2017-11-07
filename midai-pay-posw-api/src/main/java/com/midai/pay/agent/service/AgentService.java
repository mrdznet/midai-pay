package com.midai.pay.agent.service;

import java.util.List;
import java.util.Map;

import com.midai.framework.common.BaseService;
import com.midai.pay.agent.entity.Agent;
import com.midai.pay.agent.query.AgentQuery;
import com.midai.pay.agent.vo.AgentAllVo;
import com.midai.pay.agent.vo.AgentImgVo;
import com.midai.pay.agent.vo.AgentMerchantRateVo;
import com.midai.pay.agent.vo.AgentNoAndNameVo;
import com.midai.pay.agent.vo.AgentUpdateVo;
import com.midai.pay.agent.vo.AgentVo;
import com.midai.pay.common.po.ReturnVal;

public interface AgentService extends BaseService<Agent> {
	
	/**
	 * 列表查询代理商总数
	 * @param query
	 * @return
	 */
	public int queryCount(AgentQuery query);
	
	/**
	 * 列表查询代理商
	 * @param query
	 * @return
	 */
	public List<AgentVo> queryList(AgentQuery query);
	
	/**
	 * 列表查询代理商编号、代理商名称总数
	 * @param query
	 * @return
	 */
	public int queryNoAndNameCount(AgentQuery query);
	
	/**
	 * 列表查询代理商编号、代理商名称
	 * @param query
	 * @return
	 */
	public List<AgentNoAndNameVo> queryNoAndNameList(AgentQuery query);
	
	/**
	 * 列表查询目标代理商编号、代理商名称总数
	 * @param query
	 * @return
	 */
	public int queryNoAndNameTargetCount(AgentQuery query);
	
	/**
	 * 列表查询目标代理商编号、代理商名称
	 * @param query
	 * @return
	 */
	public List<AgentNoAndNameVo> queryNoAndNameTargetList(AgentQuery query);
	
	/**
	 * 列表查询代理商编号、代理商名称、顶级代理商编号、顶级代理商名称总数
	 * @param query
	 * @return
	 */
	public int queryNoAndNameTopCount(AgentQuery query);
	
	/**
	 * 列表查询代理商编号、代理商名称、顶级代理商编号、顶级代理商名称
	 * @param query
	 * @return
	 */
	public List<AgentNoAndNameVo> queryNoAndNameTopList(AgentQuery query);
	
	/**
	 * 代理商新增页面
	 * @return
	 */
	public AgentVo getInsertInfo(String currUser);
	
	/**
	 * 代理商新增
	 * @return
	 */
	public ReturnVal<String> insertAgent(AgentVo agentVo, String userName, String password, List<AgentImgVo> imgVo);
	
	/**
	 * 代理商更新页面
	 * @return
	 */
	public AgentAllVo getUpdatInfo(Integer id);
	
	/**
	 * 代理商更新
	 * @param vo
	 * @param userName
	 * @return
	 */
	public ReturnVal<String> updateAgent(AgentUpdateVo vo, String userName);
	
	/**
	 * 列表查询商户签约扣率总数
	 * @param query
	 * @return
	 */
	public int queryAgtMerRateCount(AgentQuery query);
	
	/**
	 * 列表查询商户签约扣率
	 * @param query
	 * @return
	 */
	public List<AgentMerchantRateVo> queryAgtMerRateList(AgentQuery query);
	
	/**
	 * 批量修改代理商扣率
	 * @param agentVo
	 * @param userName
	 * @return
	 */
	public ReturnVal<String> updateBatchAgentRate(List<AgentVo> agentVoList, String userName);
	public ReturnVal<String> updateRate(AgentVo agent);
	
	/**
	 * 通过代理商编号获取顶级代理商信息
	 * @param agentNo
	 * @param agentList
	 * @return
	 */
	public Agent getTopAgentByAgentNo(String agentNo, List<Agent> agentList);
	
	/**
	 * 获取所有的父代理商编号
	 * @param agentNo
	 * @return
	 */
	public List<String> fetchAllParentAgentNo(String agentNo);

	public Agent fetchAgentByUserName(String userName);
	
	public List<Agent> findAgentsByUserName(String userName);
	
	/**
	 * 代理商列表数据导出（无复选框条件）
	 * @param query
	 * @return
	 */
	public List<AgentVo> excelExport(AgentQuery query);
	
    /**
     * 代理商列表数据导出（有复选框条件）
     * @param ids
     * @return
     */
    public List<AgentVo> excelExportChecked(Integer[] ids);
    
    /**
     * 通过代理商编号获取所有的子代理商编号
     * @param agentNo
     * @return
     */
    public String getAgentNoParam(String agentNo);
    
    public List<Agent> getByAgentDeviceDeviceId(String deviceId);

	public List<Agent> getByDeviceNo(String deviceid);

	public Agent getTopAgentByNameAndParentId(String haibeiOrg, int i);
	
	/**
	 * 通过inscode 查找agentNo
	 */
	
	public String findAllAgentNoByInscode(String inscode);
	
	public List<Agent> getChildAgentList(AgentQuery query);
	
	public Map<String, Object> queryChildAgentList(AgentQuery query);

	public List<Agent> findListByLikeAgent(Agent searchAgent);

	/**
	 * 根据code，查找代理商
	 * @param string
	 * @return
	 */
	public List<Agent> selectAgentListByCodes(String string);

	/**
	 * 获取代理商信息
	 * @return
	 */
	public AgentVo getAgentInfo(String agentNo);
}
