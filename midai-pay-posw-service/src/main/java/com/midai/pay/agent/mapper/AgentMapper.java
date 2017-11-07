package com.midai.pay.agent.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.agent.entity.Agent;
import com.midai.pay.agent.query.AgentQuery;
import com.midai.pay.agent.vo.AgentMerchantRateVo;
import com.midai.pay.agent.vo.AgentNoAndNameVo;
import com.midai.pay.agent.vo.AgentVo;

public interface AgentMapper extends MyMapper<Agent> {
	
	public String columnProfit = " agent_no agentNo, name, parent_agent_id parentAgentId, parent_agent_name parentAgentName, swing_card_limit swingCardLimit "
			+ ", swing_card_debit_rate swingCardDebitRate, swing_card_credit_rate swingCardCreditRate, swing_card_settle_fee swingCardSettleFee "
			+ ", non_card_debit_rate nonCardDebitRate, non_card_credit_rate nonCardCreditRate, pos_debit_limit posDebitLimit, pos_debit_rate posDebitRate "
			+ ", pos_credit_rate posCreditRate, pos_settle_fee posSettleFee, scan_code_wx_rate scanCodeWxRate, scan_code_zfb_rate scanCodeZfbRate "
			+ ", scan_code_yl_rate scanCodeYlRate, scan_code_jdbt_rate scanCodeJdbtRate, scan_code_other_rate scanCodeOtherRate "
			+ ", scan_code_myhb_rate scanCodeMyhbRate, terminal_swipe_deposit terminalSwipeDeposit, terminal_swipe_deposit_return terminalSwipeDepositReturn "
			+ ", terminal_pos_deposit terminalPosDeposit, terminal_pos_deposit_return terminalPosDepositReturn, code, terminal_pos_deposit_monthly terminalPosDepositMonthly "
			+ ", cust_count custCount";
	
	@SelectProvider(type=com.midai.pay.agent.provider.AgentProvider.class, method="queryCount")
	public int queryCount(AgentQuery query);
	
	@SelectProvider(type=com.midai.pay.agent.provider.AgentProvider.class, method="queryList")
	public List<Agent> queryList(AgentQuery query);
	
	@SelectProvider(type=com.midai.pay.agent.provider.AgentProvider.class, method="queryNoAndNameCount")
	public int queryNoAndNameCount(AgentQuery query);
	
	@SelectProvider(type=com.midai.pay.agent.provider.AgentProvider.class, method="queryNoAndNameCountByName")
	public int queryNoAndNameCountByName(AgentQuery query);
	
	@SelectProvider(type=com.midai.pay.agent.provider.AgentProvider.class, method="queryNoAndNameList")
	public List<AgentNoAndNameVo> queryNoAndNameList(AgentQuery query);
	
	@SelectProvider(type=com.midai.pay.agent.provider.AgentProvider.class, method="queryNoAndNameListByName")
	public List<AgentNoAndNameVo> queryNoAndNameListByName(AgentQuery query);
	
	@SelectProvider(type=com.midai.pay.agent.provider.AgentProvider.class, method="queryNoAndNameTargetCount")
	public int queryNoAndNameTargetCount(AgentQuery query);
	
	@SelectProvider(type=com.midai.pay.agent.provider.AgentProvider.class, method="queryNoAndNameTargetList")
	public List<AgentNoAndNameVo> queryNoAndNameTargetList(AgentQuery query);
	
	@SelectProvider(type=com.midai.pay.agent.provider.AgentProvider.class, method="queryAgtMerRateCount")
	public int queryAgtMerRateCount(AgentQuery query);
	
	@SelectProvider(type=com.midai.pay.agent.provider.AgentProvider.class, method="queryAgtMerRateList")
	public List<AgentMerchantRateVo> queryAgtMerRateList(AgentQuery query);
	
	@UpdateProvider(type=com.midai.pay.agent.provider.AgentProvider.class, method="updateBatchAgentRate")
	public Integer updateBatchAgentRate(Map<String, List<Agent>> map);
	@UpdateProvider(type=com.midai.pay.agent.provider.AgentProvider.class, method="updateRate")
	public Integer updateRate(AgentVo agentVo);
	
	@UpdateProvider(type=com.midai.pay.agent.provider.AgentProvider.class, method="updateBatchMerRate")
	public Integer updateBatchMerRate(Map<String, List<AgentMerchantRateVo>> map);
	
	@Select("select short_name shortName, agent_levle agentLevle from tbl_bo_agent where agent_no = #{agentNo}")
	public Agent getShortNameAndLevel(String agentNo);

	@Select("select " + columnProfit +  " from tbl_bo_agent where agent_no = #{agentNo}")
	public Agent getAgentInfo(String agentNo);
	
	@Select("select agent_no agentNo, name, short_name shortName, parent_agent_id parentAgentId, agent_levle agentLevle,single_money  singleMoney  from tbl_bo_agent")
	public List<Agent> getAgentList();
	
	@Select("select agent_no agentNo from tbl_bo_agent where agent_no like concat(#{queryParam},'%') order by create_time desc limit 0,1")
	public String getLastestAgentNo(String queryParam);
	
	@SelectProvider(type=com.midai.pay.agent.provider.AgentProvider.class, method="excelExport")
	public List<Agent> excelExport(AgentQuery query);
	
	@SelectProvider(type=com.midai.pay.agent.provider.AgentProvider.class,method="fetchExcelExport")
	public List<Agent> fetchExcelExport(String ids);

	@Select(" select a.agent_no agentNo, a.name, a.merchant_rate merchantRate, a.inscode, g.agent_no createUser, g.name updateUser from tbl_bo_agent a left join tbl_bo_agent_device ad on a.agent_no=ad.agent_id INNER JOIN "
			+ " ( select ta.name, ta.agent_no, ta.inscode from tbl_bo_agent ta where ta.parent_agent_id='0' or ta.parent_agent_id is null) g on a.inscode=g.inscode "
			+ " where ad.device_no=#{deviceId} ")
	public List<Agent> getByAgentDeviceDeviceId(String deviceId);

	@Select(" SELECT AGENT_.* FROM tbl_bo_device DEVICE "
			+ " INNER JOIN tbl_bo_agent_device AGENTDEVICE ON DEVICE.device_no=AGENTDEVICE.device_no  "
			+ " INNER JOIN tbl_bo_agent AGENT_ ON AGENT_.agent_no=AGENTDEVICE.agent_id "
			+ " WHERE  DEVICE.device_no=#{deviceid}")
	public List<Agent> getByDeviceNo(String deviceid);
	
	@Select(" select inscode from tbl_bo_agent where agent_no=#{agentNo}")
	public String findInscode(String agentNo);
	
	@Select(" select id, agent_no, name, short_name, parent_agent_id, parent_agent_name, mobile, address, cost_rate, merchant_rate merchantRate, agent_levle, op_state "
			+ " apply_rate, create_user, update_user, single_money singleMoney, inscode   from tbl_bo_agent where agent_no=#{agentNo} ")
	public Agent findByAgentNo(String agentNo);
	@Select(" select agent_no from tbl_bo_agent where parent_agent_id=#{agentNo} ")
	public List<String> findAllChildAgents(String agentNo);
	
	@Select("select agent_no from tbl_bo_agent where inscode =#{inscode}")
	public List<String> findAllAgentNoByInscode(String inscode);
	
	@Select(" select  agent_no agentNo, name, short_name shortName, parent_agent_id parentAgentId, agent_levle agentLevle,single_money  singleMoney "
			+ "  from tbl_bo_agent where inscode =#{inscode} ")
	public List<Agent> findAgentInfoByInscode(String inscode);
	
	@Select(" select agent_no from tbl_bo_agent where parent_agent_id='0' AND inscode =#{inscode} ")
	public String findTopAgentNo(String inscode);
	
	@Select(" select code from tbl_bo_agent where agent_no=#{agentNo}")
	public String findCode(String agentNo);

	@SelectProvider(type=com.midai.pay.agent.provider.AgentProvider.class,method="findListByLikeAgent")
	public List<Agent> findListByLikeAgent(Agent searchAgent);

	@SelectProvider(type=com.midai.pay.agent.provider.AgentProvider.class,method="findAgentListByCodes")
	public List<Agent> findAgentListByCodes(String codes);
}
