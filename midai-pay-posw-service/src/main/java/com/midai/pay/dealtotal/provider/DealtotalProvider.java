
package com.midai.pay.dealtotal.provider;

import org.apache.commons.lang3.StringUtils;

import com.midai.pay.dealtotal.query.DealtotalQuery;

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

public class DealtotalProvider {
	public String columns = "a.host_trans_ssn hostTransSsn,a.resp_cd_loc respCdLoc, a.resp_cd_loc_dsp respCdLocDsp, a.mchnt_code_in mchntCodeIn, a.merc_no mercNo, a.mchnt_name mchntName, a.agent_name agentName, "
			+ " case a.pay_mode WHEN '1' THEN 'mpos' WHEN  '2'  THEN '无卡 ' WHEN  '3'  THEN '大pos' END  payMode, "
			+ " a.trans_time transTime, Round(a.trans_amt/100,2) transAmt, a.trans_card_no transCardNo, "
			+ " case a.card_kind WHEN '01' THEN '借记卡' WHEN  '02'  THEN '贷记卡' END  cardKind,"
			+" case a.trans_code WHEN '0200' THEN '消费' WHEN  '0400'  THEN '消费冲正' WHEN '0210' THEN '余额查询' WHEN '0500' THEN '提现申请' WHEN '0700' THEN '转账' WHEN '0600' THEN '信用卡还款' WHEN '0220' THEN '退款' END  transCode "
			;

	/**
	 * 历史交易明细查询
	 * 
	 * @param query
	 * @return
	 */
	public String queryList(DealtotalQuery query) {
		StringBuffer sql = new StringBuffer(" select " + columns + " FROM tbl_deal_total a  WHERE 1 = 1 ");

		sql.append(this.comnStringBuffer(query));
		
		sql.append(" order by a.host_trans_ssn DESC limit " + (query.getPageNumber() - 1) * query.getPageSize() + "," + query.getPageSize());
		
		return sql.toString();
	}

	public String queryCount(DealtotalQuery query) {

		StringBuffer sql = new StringBuffer(" select count(1) from tbl_deal_total a where 1 = 1 ");

		sql.append(this.comnStringBuffer(query));
		
		return sql.toString();
	}

	private StringBuffer comnStringBuffer(DealtotalQuery query) {
		StringBuffer sql = new StringBuffer("");
		if (query != null) {
			/* 交易流水号 */
			if (!StringUtils.isEmpty(query.getHostTransSsn())) {
				sql.append(" and a.host_trans_ssn like '%" + query.getHostTransSsn() + "%' ");
			}

			/* 交易类型 */
			if (!StringUtils.isEmpty(query.getTransCode())) {
				sql.append(" and a.trans_code like '%" + query.getTransCode() + "%' ");
			}
			
			/* 交易时间起 */
			if (!StringUtils.isEmpty(query.getTransTimeBegin())) {
				sql.append(" and  date_format(a.trans_time,'%Y-%m-%d') >= #{transTimeBegin}");
			}
			/* 交易时间终 */
			if (!StringUtils.isEmpty(query.getTransTimeEnd())) {
				sql.append(" and date_format(a.trans_time,'%Y-%m-%d') <= #{transTimeEnd}");
			}
			
			/* 交易金额起 */
			if (query.getTransAmtBegin() != null) {
				sql.append(" and a.trans_amt/100 >= #{transAmtBegin} ");
			}
			/* 交易金额止 */
			if (query.getTransAmtEnd() != null) {
				sql.append(" and a.trans_amt/100  <= #{transAmtEnd} ");
			}
			
			/* 商户名称 */
			if (!StringUtils.isEmpty(query.getMchntName())) {
				sql.append(" and a.mchnt_name like '%" + query.getMchntName() + "%' ");
			}
			/* 交易方式 */
			if (!StringUtils.isEmpty(query.getPayMode())) {
				sql.append(" and a.pay_mode =#{payMode} ");
			}
			
			/* 卡类型 */
			if (!StringUtils.isEmpty(query.getCardKind())) {
				sql.append(" and a.card_kind =#{cardKind} ");
			}
		}

		return sql;

	}

	/**
	 * 代理商交易查詢
	 */
	public String angetQueryList(DealtotalQuery query) {
		StringBuffer sql = new StringBuffer(" select " + columns + " FROM tbl_deal_total a  WHERE 1 = 1 ");
		sql.append(this.agentcomnStringBuffer(query));
		sql.append(" order by a.host_trans_ssn DESC limit " + (query.getPageNumber() - 1) * query.getPageSize() + ","
				+ query.getPageSize());
		return sql.toString();
	}

	public String angetQueryCount(DealtotalQuery query) {
		StringBuffer sql = new StringBuffer(" select count(1) from tbl_deal_total a where 1 = 1  ");
		sql.append(this.agentcomnStringBuffer(query));
		return sql.toString();
	}

	private StringBuffer agentcomnStringBuffer(DealtotalQuery query) {

		StringBuffer sql = new StringBuffer("");
		if (query != null) {
			/* 代理商 */
			if (!StringUtils.isEmpty(query.getAgentId())) {
				sql.append(" and a.agent_id in ( " + query.getAgentId() + "  ) ");
			}
			/* 交易流水号 */
			if (!StringUtils.isEmpty(query.getHostTransSsn())) {
				sql.append(" and a.host_trans_ssn like '%" + query.getHostTransSsn() + "%' ");
			}
			/* 交易类型 */
			if (!StringUtils.isEmpty(query.getTransCode())) {
				sql.append(" and a.trans_code like '%" + query.getTransCode() + "%' ");
			}
			/* 交易金额起 */
			if (query.getTransAmtBegin() != null) {
				sql.append(" and a.trans_amt/100 >= #{transAmtBegin} ");
			}
			/* 交易金额止 */
			if (query.getTransAmtEnd() != null) {
				sql.append(" and a.trans_amt/100  <= #{transAmtEnd} ");
			}
			/* 商户名称 */
			if (!StringUtils.isEmpty(query.getMchntName())) {
				sql.append(" and a.mchnt_name like '%" + query.getMchntName() + "%' ");
			}
			/* 交易时间起 */
			if (!StringUtils.isEmpty(query.getTransTimeBegin())) {
				sql.append(" and  date_format(a.trans_time,'%Y-%m-%d') >= #{transTimeBegin}");
			}
			/* 交易时间终 */
			if (!StringUtils.isEmpty(query.getTransTimeEnd())) {
				sql.append(" and date_format(a.trans_time,'%Y-%m-%d') <= #{transTimeEnd}");
			}
		}
		return sql;
	}

	/**
	 * 代理商交易查询Excel下载
	 */
	public String AgentExcelDownBodealtotal(DealtotalQuery query) {
		StringBuffer sql = new StringBuffer(
				" select a.host_trans_ssn hostTransSsn,a.resp_cd_loc respCdLoc, a.resp_cd_loc_dsp respCdLocDsp,a.mchnt_code_in mchntCodeIn,a.mchnt_name mchntName,"
						+ " a.mobile mobile,a.device_no_in deviceNoIn,a.trans_time transTime,Round(a.trans_amt/100,2) transAmt,a.trans_card_no transCardNo,a.card_iss_name cardIssName , case a.card_kind WHEN '01' THEN '借记卡' WHEN  '02'  THEN '贷记卡' END  cardKind,"
						+ " a.card_iss_id cardIssId,case a.trans_code WHEN '0200' THEN '消费' END  transCode,a.special_fee_type specialFeeType, case a.trans_status WHEN '0' THEN '成功' WHEN '1' THEN '已上传'WHEN '5' THEN '失败' END  transStatus ,"
						+ " a.mchnt_code_out mchntCodeOut,a.rout_inst_id_cd routInstIdCd,"
						+ " a.agent_id agentId,a.agent_name agentName "

						+ " ,i.inst_fee_rate instFeeRate, Round(i.inst_fee_max/100,2) instFeeMax, i.inst_dfree_rate instDfeeRate "

						+ " ,a.mchnt_rate mchntRate, Round(a.mchnt_single_fee/100,2) mchntSingleFee "
						+ " ,Round(g.tixian_amt/100,2) tixianAmt, Round(g.tixian_feeamt/100,2) tixianFeeamt,p.channel_code channelCode,p.tixi_logno tixiLogno "
						+ " ,a.card_kind cardKindState "

						+ "  FROM tbl_deal_total a" + "	LEFT JOIN tbl_bo_getmoney g ON a.host_trans_ssn=g.logno "
						+ "	LEFT JOIN tbl_bo_auto_paymoney p ON a.host_trans_ssn=p.tixi_logno "
						+ "  LEFT JOIN tbl_inst i ON i.inst_code=a.rout_inst_id_cd " + "  WHERE 1 = 1 ");
		sql.append(this.agentcomnStringBuffer(query));
		return sql.toString();
	}

	public String AgentExcelDownBodealtotalCount(DealtotalQuery query) {
		StringBuffer sql = new StringBuffer(" select count(1) from tbl_deal_total a where 1 = 1 ");
		sql.append(this.agentcomnStringBuffer(query));
		return sql.toString();
	}

	/**
	 * Excel下载
	 */
	public String ExcelDownBodealtotal(DealtotalQuery query) {
		StringBuffer sql = new StringBuffer(
				" select a.host_trans_ssn hostTransSsn,a.resp_cd_loc respCdLoc, a.resp_cd_loc_dsp respCdLocDsp,a.mchnt_code_in mchntCodeIn,a.mchnt_name mchntName,"
						+ " a.mobile mobile,a.device_no_in deviceNoIn,a.trans_time transTime,Round(a.trans_amt/100,2) transAmt,a.trans_card_no transCardNo,a.card_iss_name cardIssName , case a.card_kind WHEN '01' THEN '借记卡' WHEN  '02'  THEN '贷记卡' END  cardKind,"
						+ " a.card_iss_id cardIssId,case a.trans_code WHEN '0200' THEN '消费' END  transCode,a.special_fee_type specialFeeType, case a.trans_status WHEN '0' THEN '成功' WHEN '1' THEN '已上传'WHEN '5' THEN '失败' END  transStatus ,"
						+ " a.mchnt_code_out mchntCodeOut,a.rout_inst_id_cd routInstIdCd,"
						+ " a.agent_id agentId,a.agent_name agentName "

						+ " ,i.inst_fee_rate instFeeRate, Round(i.inst_fee_max/100,2) instFeeMax, i.inst_dfree_rate instDfeeRate "

						+ " ,a.mchnt_rate mchntRate, Round(a.mchnt_single_fee/100,2) mchntSingleFee "
						+ " ,Round(g.tixian_amt/100,2) tixianAmt, Round(g.tixian_feeamt/100,2) tixianFeeamt,p.channel_code channelCode,p.tixi_logno tixiLogno "
						+ " ,a.card_kind cardKindState " + " ,a.inscode inscode "

						+ "  FROM tbl_deal_total a" + "	LEFT JOIN tbl_bo_getmoney g ON a.host_trans_ssn=g.logno "
						+ "	LEFT JOIN tbl_bo_auto_paymoney p ON a.host_trans_ssn=p.tixi_logno "
						+ "  LEFT JOIN tbl_inst i ON i.inst_code=a.rout_inst_id_cd " + "  WHERE 1 = 1 ");
		sql.append(this.comnStringBuffer(query));
		return sql.toString();
	}

	public String ExcelDownBodealtotalCount(DealtotalQuery query) {
		StringBuffer sql = new StringBuffer(" select count(1) from tbl_deal_total a where 1 = 1 ");
		sql.append(this.comnStringBuffer(query));
		return sql.toString();
	}

}
