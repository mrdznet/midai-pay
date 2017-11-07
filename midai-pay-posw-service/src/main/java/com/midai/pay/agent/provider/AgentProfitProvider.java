package com.midai.pay.agent.provider;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import com.midai.pay.agent.entity.AgentProfit;

public class AgentProfitProvider {

	public final String columns = " id Id, host_trans_ssn hostTransSsn, merc_no mercNo, agent_id agentId, agent_name agentName "
			+ ", parent_agent_id parentAgentId, parent_agent_name parentAgentName, trans_amt transAmt, profit, profit_sub profitSub "
			+ ", profit_total profitTotal, profit_out profitOut, profit_sub_out profitSubOut, settlement_status settlementStatus "
			+ ", code, create_time createTime, update_time updateTime, DATE_FORMAT(settlement_time,'%Y-%m-%d') settlementTime ";

	public String batchInsert(Map<String, List<AgentProfit>> map) {
		List<AgentProfit> list = map.get("list");
		if (list == null || list.isEmpty()) {
			throw new RuntimeException("{\"errorMsg\":\"保存代理商分润信息为空！\"}");
		}
		StringBuffer sql = new StringBuffer();
		
		sql.append(" insert into tbl_bo_agent_profit(host_trans_ssn, merc_no, agent_id, agent_name, parent_agent_id, parent_agent_name"
				+ ", trans_amt, profit, profit_sub, profit_total, settlement_status, code, settlement_time, create_time, swing_card_limit "
				+ ", swing_card_debit_rate, swing_card_credit_rate, swing_card_settle_fee, non_card_debit_rate, non_card_credit_rate "
				+ ", pos_debit_limit, pos_debit_rate, pos_settle_fee, pos_credit_rate, scan_code_wx_rate, scan_code_zfb_rate "
				+ ", scan_code_yl_rate, scan_code_jdbt_rate, scan_code_other_rate, scan_code_myhb_rate, trans_type) values");
		
	        MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].hostTransSsn},#'{'list[{0}].mercNo},#'{'list[{0}].agentId}"
	        		+ " ,#'{'list[{0}].agentName},#'{'list[{0}].parentAgentId} ,#'{'list[{0}].parentAgentName} ,#'{'list[{0}].transAmt}"
	        		+ " ,#'{'list[{0}].profit},#'{'list[{0}].profitSub},#'{'list[{0}].profitTotal},#'{'list[{0}].settlementStatus}"
	        		+ " ,#'{'list[{0}].code},#'{'list[{0}].settlementTime},#'{'list[{0}].createTime},#'{'list[{0}].swingCardLimit}"
	        		+ " ,#'{'list[{0}].swingCardDebitRate},#'{'list[{0}].swingCardCreditRate},#'{'list[{0}].swingCardSettleFee},#'{'list[{0}].nonCardDebitRate}"
	        		+ " ,#'{'list[{0}].nonCardCreditRate},#'{'list[{0}].posDebitLimit},#'{'list[{0}].posDebitRate}"
	        		+ " ,#'{'list[{0}].posSettleFee},#'{'list[{0}].posCreditRate},#'{'list[{0}].scanCodeWxRate}"
	        		+ " ,#'{'list[{0}].scanCodeZfbRate},#'{'list[{0}].scanCodeYlRate},#'{'list[{0}].scanCodeJdbtRate}"
	        		+ "  ,#'{'list[{0}].scanCodeOtherRate},#'{'list[{0}].scanCodeMyhbRate},#'{'list[{0}].transType})");
	        for (int i = 0; i < list.size(); i++) {
	            sql.append(messageFormat.format(new Integer[]{i}));
	            sql.append(",");
	        }
	        sql.setLength(sql.length() - 1);
	        return sql.toString();
	}
}
