package com.midai.pay.trade.provider;

public class TradeSettleProvider {

	/**
	 * 交易结算页面的详情页
	 * @param mercId
	 * @return
	 */
	public String getPageDetailList(String mercId)
	{
		StringBuffer sql=new StringBuffer(" SELECT a.trans_time transTime,a.mchnt_code_in mchntCodeIn,a.mchnt_name mchntName,a.device_no_in deviceNoIn,a.trans_code_name transCodeName,"
				          + " a.trans_status transStatus,a.resp_cd_loc respCdLoc,a.trans_amt/100  transAmt,a.trans_card_no transCardNo,b.state state"
				          + ", case a.card_kind when '01' then '借记卡' when '02' then '贷记卡' else '其它卡类型' end cardKind "
				          + " from tbl_deal_total a LEFT JOIN tbl_bo_eticket b ON a.host_trans_ssn=b.host_trans_ssn   ");
		sql.append(" where a.mchnt_code_in="+mercId);
		sql.append("  and DATE_SUB(CURDATE(), INTERVAL 7 DAY) <=DATE_FORMAT(a.trans_time,'%Y-%m-%d') ");
		
        return sql.toString();		
	}
}