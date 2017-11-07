package com.midai.pay.agent.provider;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.midai.pay.agent.entity.Agent;
import com.midai.pay.agent.query.AgentQuery;
import com.midai.pay.agent.vo.AgentMerchantRateVo;
import com.midai.pay.agent.vo.AgentVo;

public class AgentProvider {

	public String columns = "id,agent_no agentNo,name,short_name shortName,parent_agent_id parentAgentId,parent_agent_name parentAgentName,"
			+ "mobile,address,cost_rate costRate,merchant_rate merchantRate,agent_levle agentLevle,op_state opState,apply_rate applyRate,"
			+ "create_time createTime,create_user createUser,update_time updateTime,update_user updateUser";

	public String columnProfit = " agent_no agentNo, name, parent_agent_id parentAgentId, parent_agent_name parentAgentName, swing_card_limit swingCardLimit "
			+ ", swing_card_debit_rate swingCardDebitRate, swing_card_credit_rate swingCardCreditRate, swing_card_settle_fee swingCardSettleFee "
			+ ", non_card_debit_rate nonCardDebitRate, non_card_credit_rate nonCardCreditRate, pos_debit_limit posDebitLimit, pos_debit_rate posDebitRate "
			+ ", pos_credit_rate posCreditRate, pos_settle_fee posSettleFee, scan_code_wx_rate scanCodeWxRate, scan_code_zfb_rate scanCodeZfbRate "
			+ ", scan_code_yl_rate scanCodeYlRate, scan_code_jdbt_rate scanCodeJdbtRate, scan_code_other_rate scanCodeOtherRate "
			+ ", scan_code_myhb_rate scanCodeMyhbRate, terminal_swipe_deposit terminalSwipeDeposit, terminal_swipe_deposit_return terminalSwipeDepositReturn "
			+ ", terminal_pos_deposit terminalPosDeposit, terminal_pos_deposit_return terminalPosDepositReturn, code, terminal_pos_deposit_monthly terminalPosDepositMonthly "
			+ ", cust_count custCount";
	
	public String findListByLikeAgent(Agent agent) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select id Id, agent_no agentNo, name, code from tbl_bo_agent a");
		if (agent == null) {
			sql.append(" where 1=2");
		} else {

			sql.append(" where  1=1 ");

			if (StringUtils.isNotEmpty(agent.getCode())) {

				sql.append(" and a.code like concat(#{code},'%')");
			}

			if (StringUtils.isNotEmpty(agent.getAgentNo())) {

				sql.append(" and a.agent_no like concat('%',#{agentNo},'%')");
			}
			if (StringUtils.isNotEmpty(agent.getName())) {
				sql.append(" and a.name like concat('%',#{name},'%')");
			}
			if (StringUtils.isNotEmpty(agent.getParentAgentId())) {
				sql.append(" and a.parent_agent_id like concat('%',#{parentAgentId},'%')");
			}
		}

		return sql.toString();
	}

	public String queryCount(AgentQuery query) {
		StringBuffer sql = new StringBuffer("select count(1) from tbl_bo_agent where 1 = 1");
		if (query != null) {
			if(!StringUtils.isEmpty(query.getLevel()) && query.getLevel().equals("0")){ //所有下级代理商(包括子级, 子子级...)
 				if(!StringUtils.isEmpty(query.getAllAgents())){
 					sql.append(" AND  agent_no in( " + query.getAllAgents() + " )");
 				}
 			}else{ //所有下级代理商
 				if(!StringUtils.isEmpty(query.getANo())){
 					sql.append(" AND  parent_agent_id = #{aNo} ");
 				}
 			}
 			if (StringUtils.isNotBlank(query.getName())) {
 				sql.append(" and name like concat('%',#{name},'%')");
 			}
 			
 			if (StringUtils.isNotBlank(query.getAgentNo())) {
 				sql.append(" and agent_no like concat('%',#{agentNo},'%')");
 			}
 			
 			
 			if(!StringUtils.isEmpty(query.getInscode())){
 				sql.append(" AND  inscode = #{inscode} ");
			}
		}
		return sql.toString();
	}

	public String queryList(AgentQuery query) {
		StringBuffer sql = new StringBuffer(" select id, agent_no agentNo, name, parent_agent_name parentAgentName, parent_agent_id parentAgentId, create_time createTime, "
				+ " (SELECT COUNT(1) FROM tbl_bo_customer t WHERE t.agent_id=agent_no) custCount "
				+ " from tbl_bo_agent where 1 = 1");
		if (query != null) {
			if(!StringUtils.isEmpty(query.getLevel()) && query.getLevel().equals("0")){ //所有下级代理商(包括子级, 子子级...)
 				if(!StringUtils.isEmpty(query.getAllAgents())){
 					sql.append(" AND  agent_no in( " + query.getAllAgents() + " )");
 				}
 			}else{ //所有下级代理商
 				if(!StringUtils.isEmpty(query.getANo())){
 					sql.append(" AND  parent_agent_id = #{aNo} ");
 				}
 			}
 			if (StringUtils.isNotBlank(query.getName())) {
 				sql.append(" and name like concat('%',#{name},'%')");
 			}
 			
 			if (StringUtils.isNotBlank(query.getAgentNo())) {
 				sql.append(" and agent_no like concat('%',#{agentNo},'%')");
 			}
 			
 			
 			if(!StringUtils.isEmpty(query.getInscode())){
 				sql.append(" AND  inscode = #{inscode} ");
			}
			/*if (StringUtils.isNotBlank(query.getParentAgentId())) {
				sql.append(" and parent_agent_id like concat('%',#{parentAgentId},'%')");
			}
			if (StringUtils.isNotBlank(query.getParentAgentName())) {
				sql.append(" and parent_agent_name like concat('%',#{parentAgentName},'%')");
			}*/
			
			sql.append(" order by id desc ");
			if (query.getPageSize() > 0) {
				sql.append(" limit " + (query.getPageNumber() - 1) * query.getPageSize() + "," + query.getPageSize());
			}
		}
		return sql.toString();
	}

	public String queryNoAndNameCount(AgentQuery query) {
		StringBuffer sql = new StringBuffer("select count(1) from tbl_bo_agent where 1 = 1");
		if (query != null) {
			if (!StringUtils.isEmpty(query.getInscode())) {
				sql.append(" AND  inscode = #{inscode} ");
			}
			if (StringUtils.isNotBlank(query.getAgentNo())) {
				sql.append(" and (agent_no like concat('%',#{agentNo},'%') or name like concat('%',#{agentNo},'%'))");
			}
			if (!StringUtils.isEmpty(query.getMobile())) {
				sql.append(" AND  mobile = #{mobile} ");
			}
		}
		return sql.toString();
	}

	public String queryNoAndNameCountByName(AgentQuery query) {
		StringBuffer sql = new StringBuffer("select count(1) from tbl_bo_agent where 1 = 1");
		if (query != null) {

			if (!StringUtils.isEmpty(query.getInscode())) {
				sql.append(" AND  inscode = #{inscode} ");
			}
			if (StringUtils.isNotBlank(query.getAgentNo())) {
				sql.append(" and agent_no like concat('%',#{agentNo},'%')");
			}
			if (StringUtils.isNotBlank(query.getName())) {
				sql.append(" and name like concat('%',#{name},'%')");
			}
		}
		return sql.toString();
	}

	public String queryNoAndNameList(AgentQuery query) {
		StringBuffer sql = new StringBuffer("select agent_no agentNo, name from tbl_bo_agent where 1 = 1");
		if (query != null) {

			if (!StringUtils.isEmpty(query.getInscode())) {
				sql.append(" AND  inscode = #{inscode} ");
			}
			if (StringUtils.isNotBlank(query.getAgentNo())) {
				sql.append(" and (agent_no like concat('%',#{agentNo},'%') or name like concat('%',#{agentNo},'%'))");
			}
			if (StringUtils.isNotBlank(query.getMobile())) {
				sql.append(" and mobile=#{mobile}");
			}
			if (StringUtils.isNotBlank(query.getOrder())) {
				sql.append(" order by create_time " + query.getOrder());
			} else {
				sql.append(" order by create_time desc ");
			}
			if (query.getPageSize() > 0) {
				sql.append(" limit " + (query.getPageNumber() - 1) * query.getPageSize() + "," + query.getPageSize());
			}
		}
		return sql.toString();
	}

	public String queryNoAndNameListByName(AgentQuery query) {
		StringBuffer sql = new StringBuffer("select agent_no agentNo, name from tbl_bo_agent where 1 = 1");
		if (query != null) {
			if (!StringUtils.isEmpty(query.getInscode())) {
				sql.append(" AND  inscode = #{inscode} ");
			}
			if (StringUtils.isNotBlank(query.getAgentNo())) {
				sql.append(" and agent_no like concat('%',#{agentNo},'%')");
			}
			if (StringUtils.isNotBlank(query.getName())) {
				sql.append(" and name like concat('%',#{name},'%')");
			}
			if (StringUtils.isNotBlank(query.getOrder())) {
				sql.append(" order by create_time " + query.getOrder());
			} else {
				sql.append(" order by create_time desc ");
			}
			if (query.getPageSize() > 0) {
				sql.append(" limit " + (query.getPageNumber() - 1) * query.getPageSize() + "," + query.getPageSize());
			}
		}
		return sql.toString();
	}

	public String queryNoAndNameTargetCount(AgentQuery query) {
		StringBuffer sql = new StringBuffer("select count(1) from tbl_bo_agent where agent_no not in");
		sql.append(
				"(select agent_no from tbl_bo_agent where (locate('米付', name) > 0 and agent_levle = 1) or agent_no = #{agentNor})");
		if (query != null) {
			if (!StringUtils.isEmpty(query.getInscode())) {
				sql.append(" AND  inscode = #{inscode} ");
			}
			if (StringUtils.isNotBlank(query.getAgentNo())) {
				sql.append(" and agent_no like concat('%',#{agentNo},'%')");
			}
			if (StringUtils.isNotBlank(query.getName())) {
				sql.append(" and name like concat('%',#{name},'%')");
			}
			if (StringUtils.isNotBlank(query.getOrder())) {
				sql.append(" order by create_time " + query.getOrder());
			} else {
				sql.append(" order by create_time desc ");
			}
		}
		return sql.toString();
	}

	public String queryNoAndNameTargetList(AgentQuery query) {
		StringBuffer sql = new StringBuffer("select agent_no agentNo, name from tbl_bo_agent where agent_no not in");
		sql.append(
				"(select agent_no from tbl_bo_agent where (locate('米付', name) > 0 and agent_levle = 1) or agent_no = #{agentNor})");
		if (query != null) {
			if (StringUtils.isNotBlank(query.getAgentNo())) {
				sql.append(" and agent_no like concat('%',#{agentNo},'%')");
			}
			if (StringUtils.isNotBlank(query.getName())) {
				sql.append(" and name like concat('%',#{name},'%')");
			}
			if (StringUtils.isNotBlank(query.getOrder())) {
				sql.append(" order by create_time " + query.getOrder());
			} else {
				sql.append(" order by create_time desc ");
			}
			if (query.getPageSize() > 0) {
				sql.append(" limit " + (query.getPageNumber() - 1) * query.getPageSize() + "," + query.getPageSize());
			}
		}
		return sql.toString();
	}

	public String queryAgtMerRateCount(AgentQuery query) {
		StringBuffer sql = new StringBuffer("select count(1) from tbl_bo_customer where agent_id = #{agentNo}");
		return sql.toString();
	}

	public String queryAgtMerRateList(AgentQuery query) {
		StringBuffer sql = new StringBuffer("select merc_no mercNo, merc_name mercName, fee_rate feeRate, uplmt, dfee_rate dfeeRate, single_fee singleMoney from tbl_bo_customer where state=4 and agent_id = #{agentNo}");
		if (query != null) {
			
			if (StringUtils.isNotBlank(query.getOrder())) {
				sql.append(" order by create_time " + query.getOrder());
			} else {
				sql.append(" order by create_time desc ");
			}
			if (query.getPageSize() > 0) {
				// sql.append(" limit " +
				// (query.getPageNumber()-1)*query.getPageSize()+","+query.getPageSize());
				sql.append(" limit 0, 999999");
			}
		}
		return sql.toString();
	}

	public String updateBatchAgentRate(Map<String, List<Agent>> map) {
		List<Agent> list = map.get("list");
		if (list == null || list.isEmpty()) {
			throw new RuntimeException("{\"errorMsg\":\"批量修改扣率信息为空！\"}");
		}
		StringBuffer sql = new StringBuffer();
		sql.append("update tbl_bo_agent set cost_rate = case id ");
		MessageFormat messageFormat = new MessageFormat("when #'{'list[{0}].id} then #'{'list[{0}].costRate} ");
		for (int i = 0; i < list.size(); i++) {
			sql.append(messageFormat.format(new Integer[] { i }));
		}
		sql.append("end, merchant_rate = case id ");
		messageFormat = new MessageFormat("when #'{'list[{0}].id} then #'{'list[{0}].merchantRate} ");
		for (int i = 0; i < list.size(); i++) {
			sql.append(messageFormat.format(new Integer[] { i }));
		}
		sql.append("end, update_user = case id ");
		messageFormat = new MessageFormat("when #'{'list[{0}].id} then #'{'list[{0}].updateUser} ");
		for (int i = 0; i < list.size(); i++) {
			sql.append(messageFormat.format(new Integer[] { i }));
		}
		sql.append("end where id in(");
		messageFormat = new MessageFormat("#'{'list[{0}].id},");
		for (int i = 0; i < list.size(); i++) {
			sql.append(messageFormat.format(new Integer[] { i }));
		}
		sql.setLength(sql.length() - 1);
		return sql.toString() + ")";
	}

	public String updateRate(AgentVo agentVo) {
		StringBuffer sql = new StringBuffer();
		sql.append("update tbl_bo_agent set ");
		
		//刷卡成本信息
		sql.append(" swing_card_limit=#{swingCardLimit}, swing_card_debit_rate=#{swingCardDebitRate}, swing_card_credit_rate=#{swingCardCreditRate}, swing_card_settle_fee=#{swingCardSettleFee}, ");
		
		//无卡成本信息
		sql.append(" non_card_debit_rate=#{nonCardDebitRate}, non_card_credit_rate=#{nonCardCreditRate}, ");
		
		//传统POS成本信息
		sql.append(" pos_debit_limit=#{posDebitLimit}, pos_debit_rate=#{posDebitRate}, pos_credit_rate=#{posCreditRate}, pos_settle_fee=#{posSettleFee}, ");
		
		//扫码成本信息
		sql.append(" scan_code_wx_rate=#{scanCodeWxRate}, scan_code_zfb_rate=#{scanCodeZfbRate}, scan_code_yl_rate=#{scanCodeYlRate}, scan_code_jdbt_rate=#{scanCodeJdbtRate}, scan_code_other_rate=#{scanCodeOtherRate}, scan_code_myhb_rate=#{scanCodeMyhbRate}, ");
		
		//机具押金
		sql.append(" terminal_swipe_deposit=#{terminalSwipeDeposit}, terminal_swipe_deposit_return=#{terminalSwipeDepositReturn}, terminal_pos_deposit=#{terminalPosDeposit}, terminal_pos_deposit_return=#{terminalPosDepositReturn}, terminal_pos_deposit_monthly=#{terminalPosDepositMonthly} ");
		
		sql.append(" where id in ( "+agentVo.getIds()+")");
		return sql.toString();
	}

	public String updateBatchMerRate(Map<String, List<AgentMerchantRateVo>> map) {
		List<AgentMerchantRateVo> list = map.get("list");
		if (list == null || list.isEmpty()) {
			throw new RuntimeException("{\"errorMsg\":\"批量修改扣率信息为空！\"}");
		}
		StringBuffer sql = new StringBuffer();
		sql.append("update tbl_bo_customer set fee_rate = case merc_no ");
		MessageFormat messageFormat = new MessageFormat("when #'{'list[{0}].mercNo} then #'{'list[{0}].feeRate} ");
		for (int i = 0; i < list.size(); i++) {
			sql.append(messageFormat.format(new Integer[] { i }));
		}
		sql.append("end, single_fee = case merc_no ");
		messageFormat = new MessageFormat("when #'{'list[{0}].mercNo} then #'{'list[{0}].singleMoney} ");
		for (int i = 0; i < list.size(); i++) {
			sql.append(messageFormat.format(new Integer[] { i }));
		}
		sql.append("end, dfee_rate = case merc_no ");
		messageFormat = new MessageFormat("when #'{'list[{0}].mercNo} then #'{'list[{0}].dfeeRate} ");
		for (int i = 0; i < list.size(); i++) {
			sql.append(messageFormat.format(new Integer[] { i }));
		}
		sql.append("end where merc_no in(");
		messageFormat = new MessageFormat("#'{'list[{0}].mercNo},");
		for (int i = 0; i < list.size(); i++) {
			sql.append(messageFormat.format(new Integer[] { i }));
		}
		sql.setLength(sql.length() - 1);
		return sql.toString() + ")";
	}

	public String excelExport(AgentQuery query) {
		StringBuffer sql = new StringBuffer(" select id, agent_no agentNo, name, parent_agent_name parentAgentName, parent_agent_id parentAgentId, create_time createTime, "
				+ " (SELECT COUNT(1) FROM tbl_bo_customer t WHERE t.agent_id=agent_no) custCount "
				+ " from tbl_bo_agent where 1 = 1");
		if (query != null) {

			if(!StringUtils.isEmpty(query.getLevel()) && query.getLevel().equals("0")){ //所有下级代理商(包括子级, 子子级...)
 				if(!StringUtils.isEmpty(query.getAllAgents())){
 					sql.append(" AND  agent_no in( " + query.getAllAgents() + " )");
 				}
 			}else{ //所有下级代理商
 				if(!StringUtils.isEmpty(query.getANo())){
 					sql.append(" AND  parent_agent_id = #{aNo} ");
 				}
 			}
 			if (StringUtils.isNotBlank(query.getName())) {
 				sql.append(" and name like concat('%',#{name},'%')");
 			}
 			
 			if (StringUtils.isNotBlank(query.getAgentNo())) {
 				sql.append(" and agent_no like concat('%',#{agentNo},'%')");
 			}
 			
 			
 			if(!StringUtils.isEmpty(query.getInscode())){
 				sql.append(" AND  inscode = #{inscode} ");
			}
		}
		return sql.toString();
	}

	public String fetchExcelExport(String ids) {
		if (StringUtils.isBlank(ids)) {
			throw new RuntimeException("{\"errorMsg\":\"选中数据空！\"}");
		}
		StringBuffer sql = new StringBuffer("select " + columns + " from tbl_bo_agent where id in(" + ids + ")");
		return sql.toString();
	}
	
	public String findAgentListByCodes(String codes) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select " + columnProfit );
		sql.append(" from tbl_bo_agent ");
		sql.append(" where code in (" + codes + ")");
		return sql.toString();
	}

}
