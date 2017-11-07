package com.midai.pay.agent.provider;

import org.apache.commons.lang3.StringUtils;

import com.midai.pay.agent.query.AgentProfitQuery;

public class AgentProfitDetailProvider {

	public final String columns = " p.settlement_time settlementTime, p.agent_id agentId, p.merc_no mercNo, "
			+ " t.trans_card_no transCardNo, t.card_kind cardKind, (p.profit_total/100) profitTotal, (p.profit/100) profit, p.host_trans_ssn hostTransSsn, p.trans_type transType ";
	
	public String findListByAgentProfitDetailQuery(AgentProfitQuery query) {
		
		StringBuffer sb = new StringBuffer();
		sb.append(" select " + columns);
		sb.append(commonSql(query));
		sb.append(" order by p.settlement_time desc ");
		if (query.getPageSize() > 0) {
			sb.append(" limit " + (query.getPageNumber()-1)*query.getPageSize()+","+query.getPageSize());
		}
		
		return sb.toString();
	}
	
	public String findCountByAgentProfitDetailQuery(AgentProfitQuery query) {
		
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(1) " );
		sb.append(commonSql(query));
		return sb.toString();
		
	}
	
	private String commonSql(AgentProfitQuery query) {
		
		StringBuffer sb = new StringBuffer();
		sb.append(" from tbl_bo_agent_profit p INNER JOIN ");
		sb.append(" (select tt.trans_card_no, (case when tt.card_kind='01' then '借记卡' when tt.card_kind='02' then '贷记卡' end) card_kind, tt.host_trans_ssn from tbl_deal_total tt ");
		sb.append(" UNION select q.card_no trans_card_no, '扫码' card_kind, q.seq_id host_trans_ssn  from tbl_deal_total_quick q) t ");
		sb.append(" on p.host_trans_ssn=t.host_trans_ssn where 1=1");
		if(query != null) {
			
			if(StringUtils.isNotEmpty(query.getCode())) {
				String[] codeArr = query.getCode().split(",");
				sb.append(" and ( ");
				StringBuffer subStr = new StringBuffer();
				for(String code : codeArr) {
					subStr.append(" p. code like '" + code + "%' ").append("or");
				}
				if(subStr.length() > 0) {
					subStr.setLength(subStr.length()-2);
				}
				sb.append(subStr);
				sb.append(" )");
			}
			
			if(StringUtils.isNotEmpty(query.getAgentId())) {
				sb.append(" and p.agent_id like '%" + query.getAgentId() + "%' ");
			}
			
			if(StringUtils.isNotEmpty(query.getMercNo())) {
				sb.append(" and p.merc_no like '%" + query.getMercNo() + "%' ");
			}
			
			if(StringUtils.isNotEmpty(query.getSettlementTimeStart())) {
				String startDate = query.getSettlementTimeStart();
				startDate += " 00:00:00";
				sb.append(" and p.settlement_time >= '" + startDate + "'");
			}
			
			if(StringUtils.isNotEmpty(query.getSettlementTimeEnd())) {
				String endDate = query.getSettlementTimeEnd();
				endDate += " 23:59:59";
				sb.append(" and p.settlement_time <= '" + endDate + "'");
			}
		}
		
		return sb.toString();
	}
	
	public String queryAgentProfitDetailInfo(AgentProfitQuery query) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) transCount, sum(p.profit_total) profitTotalAmount, sum(p.profit) profitRetainedAmount");
		sql.append(commonSql(query));
		return sql.toString();
	}
}
