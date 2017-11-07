package com.midai.pay.agent.provider;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.midai.pay.agent.common.enums.AgentProfitCustomerGradeEnum;
import com.midai.pay.agent.query.AgentProfitQuery;
import com.midai.pay.agent.vo.AgentProfitVo;

public class AgentProfitStatisticsProvider {

	public final String columns = " DATE_FORMAT(settlement_time,'%Y-%m-%d') settlementTime, agent_id agentId, parent_agent_id parentAgentId "
			+ ", parent_agent_name parentAgentName, sum(trans_amt)/100 transAmt, sum(profit)/100 profit, sum(profit_sub)/100 profitSub "
			+ ", sum(profit_total)/100 profitTotal, sum(profit_out)/100 profitOut, sum(profit_sub_out)/100 profitSubOut "
			+ ", (select (case when count(1) > 0 then 0 else 1 end)  settlement_status "
			+ "  from tbl_bo_agent_profit t where t.settlement_status=0 and DATE_FORMAT(t.settlement_time,'%Y-%m-%d') = DATE_FORMAT(p.settlement_time,'%Y-%m-%d')"
			+ " and (t.`code` like CONCAT(p.code,'-','%') or t.`code` = p.code)) settlementStatus "
			+ ", code, count(1)  transCount ";
	
	public String queryListByQuery(AgentProfitQuery query) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select " + columns);
		sql.append(" from tbl_bo_agent_profit p where 1=1 ");
		if (query != null) {


			if(StringUtils.isNotEmpty(query.getCode())) {
				String[] codeArr = query.getCode().split(",");
				sql.append(" and ( ");
				StringBuffer subStr = new StringBuffer();
				for(String code : codeArr) {
					subStr.append(" code like '" + code + "%' ").append("or");
				}
				if(subStr.length() > 0) {
					subStr.setLength(subStr.length()-2);
				}
				sql.append(subStr);
				sql.append(" )");
			}
			
			if (StringUtils.isNotEmpty(query.getSettlementTimeStart())) {
				String startDate = query.getSettlementTimeStart();
				startDate += " 00:00:00";
				sql.append(" and settlement_time >= '" + startDate + "'");
			}

			if (StringUtils.isNotEmpty(query.getSettlementTimeEnd())) {
				String endDate = query.getSettlementTimeEnd();
				endDate += " 23:59:59";
				sql.append(" and settlement_time <= '" + endDate + "'");
			}
		}
		sql.append(" group by agent_id, parent_agent_id, DATE_FORMAT(settlement_time,'%Y-%m-%d') ");
		
		
		sql.append(" order by settlement_time desc ");

		return sql.toString();
	}

	public String findListByAgentProfitQuery(AgentProfitQuery query) {

		StringBuffer sb = new StringBuffer();
		sb.append(" select " + columns);
		sb.append(commonSql(query));
		
		sb.append(" order by settlement_time desc ");
		if (query.getPageSize() > 0) {
			sb.append(" limit " + (query.getPageNumber() - 1) * query.getPageSize() + "," + query.getPageSize());
		}

		return sb.toString();
	}

	public String findCountByAgentProfitQuery(AgentProfitQuery query) {

		StringBuffer sb = new StringBuffer();
		sb.append(" select count(1) ");
		sb.append(" from ( select " + columns );
		
		sb.append(commonSql(query));
		sb.append(" ) s");
		
		return sb.toString();

	}

	private String commonSql(AgentProfitQuery query) {


		StringBuffer sql = new StringBuffer();
		
		
		sql.append(" from tbl_bo_agent_profit p where 1=1 ");
		if (query != null) {

			if(query.getFlag().equals(AgentProfitCustomerGradeEnum.GRADE_BJ.getCode())) {
				if(StringUtils.isNotEmpty(query.getAgentId())) {
					sql.append(" and agent_id like concat('%',#{agentId},'%')");
				}
				if(StringUtils.isNotEmpty(query.getAgentName())) {
					sql.append(" and agent_name like concat('%',#{agentName},'%')");
				}
			} else if(query.getFlag().equals(AgentProfitCustomerGradeEnum.GRADE_XJ.getCode())) {
				if(StringUtils.isNotEmpty(query.getAgentId())) {
					sql.append(" and parent_agent_id like concat('%',#{agentId},'%')");
				}
				if(StringUtils.isNotEmpty(query.getAgentName())) {
					sql.append(" and parent_agent_name like concat('%',#{agentName},'%')");
				}
			}
			
			
			
			if (StringUtils.isNotEmpty(query.getSettlementTimeStart())) {
				String startDate = query.getSettlementTimeStart();
				startDate += " 00:00:00";
				sql.append(" and settlement_time >= '" + startDate + "'");
			}

			if (StringUtils.isNotEmpty(query.getSettlementTimeEnd())) {
				String endDate = query.getSettlementTimeEnd();
				endDate += " 23:59:59";
				sql.append(" and settlement_time <= '" + endDate + "'");
			}
		}
		sql.append(" group by agent_id, parent_agent_id, DATE_FORMAT(settlement_time,'%Y-%m-%d') ");
		
		return sql.toString();
	}
	
	
	
	public String queryAgentProfitStatisticsInfo(AgentProfitQuery query) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(1) transCount, sum(p.profit_total) profitTotalAmount, sum(p.profit) profitRetainedAmount");
		sb.append(" from tbl_bo_agent_profit p where 1=1 ");
		return sb.toString();
	}

	public String updateAgentProfit(Map<String, List<AgentProfitVo>> batchData) {
		List<AgentProfitVo> list = batchData.get("list");
		StringBuffer sql = new StringBuffer(
				"update tbl_bo_agent_profit set  profit_out=profit, profit_sub_out=profit_sub, settlement_status=1 where settlement_status=0 ");
		if (list != null && list.size() > 0) {
			if (list.size() <= 1) {
				sql.append(" and DATE_FORMAT(settlement_time,'%Y-%m-%d')='" + list.get(0).getSettlementTime()
						+ "' and (code like '" + list.get(0).getCode() + "-%' or code = '" + list.get(0).getCode() + "')");
			} else {

				sql.append(" and (");

				for (AgentProfitVo av : list) {
					sql.append(" ( DATE_FORMAT(settlement_time,'%Y-%m-%d')='" + av.getSettlementTime()
							+ "' and ( code like '" + av.getCode() + "%' or  code = '" + av.getCode() + "')) ");
					sql.append("or");
				}
				sql.setLength(sql.length() - 2);
				sql.append(")");
			}
		} else {
			sql.append(" and 1=2");
		}

		return sql.toString();

	}

	public String findListByParentAgent(AgentProfitVo pv) {
		
		AgentProfitQuery query = new AgentProfitQuery();
		
		query.setParentAgentId(pv.getAgentId());
		query.setSettlementTimeEnd(pv.getSettlementTime());
		query.setSettlementTimeStart(pv.getSettlementTime());
		StringBuffer sb = new StringBuffer();
		sb.append(" select " + columns);
		sb.append(commonSql(query));
		return sb.toString();
	}
}
