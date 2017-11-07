package com.midai.pay.agent.mapper;

import org.apache.ibatis.annotations.Update;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.agent.entity.AgentTemplet;

public interface AgentTempletMapper extends MyMapper<AgentTemplet> {
	
	@Update("update tbl_bo_agent_templet set "
			+ " swing_card_limit=#{swingCardLimit}, swing_card_debit_rate=#{swingCardDebitRate}, swing_card_credit_rate=#{swingCardCreditRate}, swing_card_settle_fee=#{swingCardSettleFee},"
			+ " non_card_debit_rate=#{nonCardDebitRate}, non_card_credit_rate=#{nonCardCreditRate},"
			+ " pos_debit_limit=#{posDebitLimit}, pos_debit_rate=#{posDebitRate}, pos_credit_rate=#{posCreditRate}, pos_settle_fee=#{posSettleFee},"
			+ " scan_code_wx_rate=#{scanCodeWxRate}, scan_code_zfb_rate=#{scanCodeZfbRate}, scan_code_yl_rate=#{scanCodeYlRate}, scan_code_jdbt_rate=#{scanCodeJdbtRate}, scan_code_other_rate=#{scanCodeOtherRate}, scan_code_myhb_rate=#{scanCodeMyhbRate},"
			+ " terminal_swipe_deposit=#{terminalSwipeDeposit}, terminal_swipe_deposit_return=#{terminalSwipeDepositReturn}, terminal_pos_deposit=#{terminalPosDeposit}, terminal_pos_deposit_return=#{terminalPosDepositReturn}, terminal_pos_deposit_monthly=#{terminalPosDepositMonthly},"
			+ " profit_mpos=#{profitMpos}, profit_no_card=#{profitNoCard}, profit_terminal_pos=#{profitTerminalPos}, profit_scan_code=#{profitScanCode} "
			+ " where agent_no=#{agentNo} and type=#{type}")
	int updateAgentTemplet(AgentTemplet templet);
	
	@Update("update tbl_bo_agent_templet set "
			+ " swing_card_limit=#{swingCardLimit}, swing_card_debit_rate=#{swingCardDebitRate}, swing_card_credit_rate=#{swingCardCreditRate}, swing_card_settle_fee=#{swingCardSettleFee},"
			+ " non_card_debit_rate=#{nonCardDebitRate}, non_card_credit_rate=#{nonCardCreditRate},"
			+ " pos_debit_limit=#{posDebitLimit}, pos_debit_rate=#{posDebitRate}, pos_credit_rate=#{posCreditRate}, pos_settle_fee=#{posSettleFee},"
			+ " scan_code_wx_rate=#{scanCodeWxRate}, scan_code_zfb_rate=#{scanCodeZfbRate}, scan_code_yl_rate=#{scanCodeYlRate}, scan_code_jdbt_rate=#{scanCodeJdbtRate}, scan_code_other_rate=#{scanCodeOtherRate}, scan_code_myhb_rate=#{scanCodeMyhbRate},"
			+ " profit_mpos=#{profitMpos}, profit_no_card=#{profitNoCard}, profit_terminal_pos=#{profitTerminalPos}, profit_scan_code=#{profitScanCode} "
			+ " where agent_no=#{agentNo} and type=#{type}")
	int updateCustomerTemplet(AgentTemplet templet);
	
}
