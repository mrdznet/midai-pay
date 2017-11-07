
package com.midai.pay.dealtotal.provider;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.midai.pay.dealtotal.query.DealtotalQuickQuery;

/**
 * ClassName:User <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年9月21日 <br/>
 * 
 * @author zt
 * @version
 * @since JDK 1.7
 * @see
 */

public class DealtotalQuickProvider {
	
	public String columns = " q.seq_id seqId, CONCAT(q.pay_result,',',q.t0_resp_code) payResult, CONCAT(q.pay_desc,',',q.t0_resp_desc) payDesc, q.merc_no mercNo, q.merc_name mercName, q.agent_no agentNo, a.name agentName,"
			+ " case q.trans_channel WHEN '1' THEN '微信' WHEN  '2'  THEN '支付宝 ' WHEN  '3' THEN '银联扫码' WHEN  '4'  THEN '花呗' WHEN  '5'  THEN '京东白条' END  transChannel, q.trans_channel transChannelCode, "
			+ " q.trans_amt/100 transAmt, q.create_time createTime "
			;

	public String queryList(DealtotalQuickQuery query) {
		StringBuffer sql = new StringBuffer(" select " + columns + " FROM tbl_deal_total_quick q LEFT JOIN tbl_bo_agent a on q.agent_no=a.agent_no    WHERE 1 = 1 ");

		sql.append(this.queryPara(query));
		
		sql.append(" order by q.id DESC limit " + (query.getPageNumber() - 1) * query.getPageSize() + "," + query.getPageSize());
		
		return sql.toString();
	}

	public String queryCount(DealtotalQuickQuery query) {
		StringBuffer sql = new StringBuffer(" select count(1) from tbl_deal_total_quick q where 1 = 1 ");

		sql.append(this.queryPara(query));
		
		return sql.toString();
	}

	private StringBuffer queryPara(DealtotalQuickQuery query) {
		StringBuffer sql = new StringBuffer("");
		if (query != null) {
			if (!StringUtils.isEmpty(query.getAgentId())) {
				sql.append(" and q.agent_no in (" + query.getAgentId() + ") ");
			}
			
			
			/* 交易流水号 */
			if(!StringUtils.isEmpty(query.getSeqId())){
				sql.append(" and q.seq_id like '%" + query.getSeqId() + "%' ");
			}
			/* 商户名称 */
			if (!StringUtils.isEmpty(query.getMercName())) {
				sql.append(" and q.merc_name like '%" + query.getMercName().trim() + "%' ");
			}
			/* 交易时间起 */
			if (!StringUtils.isEmpty(query.getTransTimeBegin())) {
				sql.append(" and  date_format(q.create_time,'%Y-%m-%d') >= #{transTimeBegin}");
			}
			/* 交易时间终 */
			if (!StringUtils.isEmpty(query.getTransTimeEnd())) {
				sql.append(" and date_format(q.create_time,'%Y-%m-%d') <= #{transTimeEnd}");
			}
			/* 交易金额起 */
			if (query.getTransAmtBegin() != null) {
				sql.append(" and q.trans_amt >= #{transAmtBegin} * 100");
			}
			/* 交易金额止 */
			if (query.getTransAmtEnd() != null) {
				sql.append(" and q.trans_amt <= #{transAmtEnd}  * 100");
			}
			/* 交易方式 */
			if (!StringUtils.isEmpty(query.getTransChannel())) {
				sql.append(" and q.trans_channel = '" + query.getTransChannel().trim() + "' ");
			}
		}

		return sql;
	}

	
	public String queryQuickPayRecord(Map<String, String> param) {
		String phonenumber = param.get("phonenumber");
		String queryDate = param.get("queryDate");
    	String transChanel = param.get("transChanel");
		StringBuffer sql = new StringBuffer(" SELECT a.create_time payTime, a.trans_amt payMoney, a.card_no accountNo,a.pay_result state,a.pay_desc payStatestr from tbl_deal_total_quick a  ");
		sql.append(" where a.trans_channel = " + transChanel + " and a.type = 1 and a.in_mobile = " + phonenumber + " and a.source = 1 ");
		sql.append(" and DATE_FORMAT(a.create_time,'%Y-%m') = DATE_FORMAT('" + queryDate +"','%Y-%m') order by a.create_time desc  ");
		
		return sql.toString();
	}
}
