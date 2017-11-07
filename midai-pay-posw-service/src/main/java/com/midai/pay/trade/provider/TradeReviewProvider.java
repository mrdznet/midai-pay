package com.midai.pay.trade.provider;

import org.springframework.util.StringUtils;

import com.midai.pay.trade.query.TradeReviewQuery;

public class TradeReviewProvider {

	public String findQuery(TradeReviewQuery query){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT t.eticket_path bigImgUrl, case when c.mer_type=0 then '个人' when c.mer_type=0 then '个人' else '普通商户' end mercType, t.host_trans_ssn hostTransSsn, ");
		sql.append(" case when e.state=1 then '通过' when e.state=2 then '拒绝' else '待审' end state, ");
		sql.append(" case when 	c.merc_id is null then '' else c.merc_id end mercId, ROUND(t.trans_amt/100,2)  money, case when c.merc_name is null then '' else c.merc_name end mercName ");
		sql.append(" ,case t.card_kind when '01' then '借记卡' when '02' then '贷记卡' else '未知类型' end cardKind ");
		
		sql.append(" FROM tbl_deal_total t ");
		sql.append(" LEFT JOIN tbl_bo_customer c ON t.mchnt_code_in=c.merc_id ");
		sql.append(" LEFT JOIN tbl_bo_eticket e ON t.host_trans_ssn=e.host_trans_ssn 	WHERE t.trans_status=0 and t.trans_code = '0200' and t.rout_inst_id_cd <> '00000081'");
		
		if(null != query){
			
 			if(!StringUtils.isEmpty(query.getInscode())){
 				sql.append(" AND t.inscode = #{inscode} ");
			}
			
			if(!StringUtils.isEmpty(query.getHostTransSsn())){	//流水号
				sql.append(" AND t.host_trans_ssn LIKE '%" + query.getHostTransSsn() +"%' ");
			}
			if(!StringUtils.isEmpty(query.getMercId())){	//商户小票号
				sql.append(" AND t.mchnt_code_in LIKE '%" + query.getMercId() + "%' ");
			}
			if(!StringUtils.isEmpty(query.getTerminalNo())){//终端编号
				sql.append(" AND t.device_no_in LIKE '%" + query.getTerminalNo() + "%' ");
			}
			
			if(!StringUtils.isEmpty(query.getState()) && 0 != query.getState()){//交易审核状态
				sql.append(" AND e.state=#{state}");
			}else if(!StringUtils.isEmpty(query.getState()) && 0 == query.getState()){
				sql.append(" AND (e.state is null || e.state=0)");
			}
			
			if(!StringUtils.isEmpty(query.getMercType())){//商户类型
				sql.append(" AND c.mer_type=#{mercType}");
			}
			if(!StringUtils.isEmpty(query.getMercName())){//商户名称
				sql.append(" AND t.mchnt_name LIKE '%" + query.getMercName() + "%' ");
			}
			if(!StringUtils.isEmpty(query.getTransDateStart())){//交易日期-起
				sql.append(" AND t.trans_time >= str_to_date('" + query.getTransDateStart() + "', '%Y-%m-%d %H:%i:%s') ");
			}
			if(!StringUtils.isEmpty(query.getTransDateEnd())){//交易日期-止
				sql.append(" AND t.trans_time <= str_to_date('" + query.getTransDateEnd() + "', '%Y-%m-%d %H:%i:%s') ");
			}
			if(!StringUtils.isEmpty(query.getTransAmtStart())){//交易金额-起
				sql.append(" AND t.trans_amt/100 >= #{transAmtStart} ");
			}
			if(!StringUtils.isEmpty(query.getTransAmtEnd())){//交易金额-止
				sql.append(" AND t.trans_amt/100 <= #{transAmtEnd} ");
			}
			
			if (!StringUtils.isEmpty(query.getOrder())) {
				sql.append(" order by t.create_time " + query.getOrder());
			} else {
				sql.append(" order by t.create_time desc ");
			}
			if (query.getPageSize() > 0) {
				sql.append(" limit " + (query.getPageNumber()-1)*query.getPageSize()+","+query.getPageSize());
			}
		}
		
		return sql.toString();
	}
	
	public String findQueryCount(TradeReviewQuery query){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT count(1) ");
		sql.append(" FROM tbl_deal_total t ");
		sql.append(" LEFT JOIN tbl_bo_customer c ON t.mchnt_code_in=c.merc_id ");
		sql.append(" LEFT JOIN tbl_bo_eticket e ON t.host_trans_ssn=e.host_trans_ssn 	WHERE t.trans_status=0 and t.trans_code = '0200' and t.rout_inst_id_cd <> '00000081' ");
		
		if(null != query){
 			if(!StringUtils.isEmpty(query.getInscode())){
 				sql.append(" AND t.inscode = #{inscode} ");
			}
 			
			if(!StringUtils.isEmpty(query.getHostTransSsn())){	//流水号
				sql.append(" AND t.host_trans_ssn LIKE '%" + query.getHostTransSsn() +"%' ");
			}
			if(!StringUtils.isEmpty(query.getMercId())){	//商户小票号
				sql.append(" AND t.mchnt_code_in LIKE '%" + query.getMercId() + "%' ");
			}
			if(!StringUtils.isEmpty(query.getTerminalNo())){//终端编号
				sql.append(" AND t.device_no_in LIKE '%" + query.getTerminalNo() + "%' ");
			}
			
			if(!StringUtils.isEmpty(query.getState()) && 0 != query.getState()){//交易审核状态
				sql.append(" AND e.state=#{state}");
			}else if(!StringUtils.isEmpty(query.getState()) && 0 == query.getState()){
				sql.append(" AND (e.state is null || e.state=0)");
			}
			
			if(!StringUtils.isEmpty(query.getMercType())){//商户类型
				sql.append(" AND c.mer_type=#{mercType}");
			}
			if(!StringUtils.isEmpty(query.getMercName())){//商户名称
				sql.append(" AND t.mchnt_name LIKE '%" + query.getMercName() + "%' ");
			}
			if(!StringUtils.isEmpty(query.getTransDateStart())){//交易日期-起
				sql.append(" AND t.trans_time >= str_to_date('" + query.getTransDateStart() + "', '%Y-%m-%d %H:%i:%s') ");
			}
			if(!StringUtils.isEmpty(query.getTransDateEnd())){//交易日期-止
				sql.append(" AND t.trans_time <= str_to_date('" + query.getTransDateEnd() + "', '%Y-%m-%d %H:%i:%s') ");
			}
			if(!StringUtils.isEmpty(query.getTransAmtStart())){//交易金额-起
				sql.append(" AND t.trans_amt/100 >= #{transAmtStart} ");
			}
			if(!StringUtils.isEmpty(query.getTransAmtEnd())){//交易金额-止
				sql.append(" AND t.trans_amt/100 <= #{transAmtEnd} ");
			}
		}
		
		return sql.toString();
	}
	
	public String findCardInfo(String hostTransSsn){
		String sql = " SELECT t.mchnt_name mchntName, m.inst_merc_id mercNo, t.device_no_in deviceNoIn, '01' optUser, t.trans_card_no transCardNo, '2000/00' cardValid, t.card_iss_name accountName, "
				+ "	'000001' batchNum, t.trans_code_name transCodeName, t.host_trans_ssn hostTransSsn, t.device_ssn_out deviceSsnOut, '000001' respAuthId, DATE_FORMAT(t.trans_time,'%Y-%m-%d %H:%i:%s') transTime, ROUND(t.trans_amt/100,2)  transAmt, "
					+"		  t.sign_path signPath	  "
					+" FROM tbl_deal_total t LEFT JOIN tbl_bo_customer c ON t.mchnt_code_in=c.merc_id"
					+" left join tbl_inst_merc m on t.mchnt_code_in=m.merc_id and t.rout_inst_id_cd=m.inst_code "
					+" WHERE t.host_trans_ssn=#{hostTransSsn}";
		
		return sql;
	}
	
	public String findAllByHostTransSsn(String hostTransSsn){
		String sql = " SELECT t.host_trans_ssn hostTransSsn, t.trans_amt transAmt, t.trans_time transTime,"
					+" 			c.merc_id mercId, t.card_kind carType, t.mchnt_rate mchntRate, t.mchnt_single_fee mchntSingleFee "
					+" FROM tbl_deal_total t "
					+" LEFT JOIN tbl_bo_customer c ON t.mchnt_code_in=c.merc_id "
					+" WHERE t.host_trans_ssn IN ('" + hostTransSsn + "')";
		return sql;
	}
	
	public String channelEticketQuery(TradeReviewQuery query){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT e.channel_pic bigImgUrl, case when c.mer_type=0 then '个人' when c.mer_type=0 then '个人' else '普通商户' end mercType, t.host_trans_ssn hostTransSsn, ");
		sql.append(" case when e.state=1 then '通过' when e.state=2 then '拒绝' else '待审' end state, ");
		sql.append(" case when 	c.merc_id is null then '' else c.merc_id end mercId, ROUND(t.trans_amt/100,2)  money, case when c.merc_name is null then '' else c.merc_name end mercName ");
		sql.append(" ,case t.card_kind when '01' then '借记卡' when '02' then '贷记卡' else '未知类型' end cardKind ");
		
		sql.append(" FROM tbl_deal_total t ");
		sql.append(" LEFT JOIN tbl_bo_customer c ON t.mchnt_code_in=c.merc_id ");
		sql.append(" LEFT JOIN tbl_bo_eticket e ON t.host_trans_ssn=e.host_trans_ssn ");
		sql.append(" WHERE t.trans_status=0 and t.trans_code = '0200' ");
		
		if(null != query){
 			if(!StringUtils.isEmpty(query.getInscode())){
 				sql.append(" AND t.inscode = #{inscode} ");
			}
			
			if(!StringUtils.isEmpty(query.getHostTransSsn())){	//流水号
				sql.append(" AND t.host_trans_ssn LIKE '%" + query.getHostTransSsn() +"%' ");
			}
			
			if (query.getPageSize() > 0) {
				sql.append(" limit " + (query.getPageNumber()-1)*query.getPageSize()+","+query.getPageSize());
			}
		}
		
		return sql.toString();
	}
	
	public String channelEticketCount(TradeReviewQuery query){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT count(1) ");
		sql.append(" FROM tbl_deal_total t ");
		sql.append(" LEFT JOIN tbl_bo_customer c ON t.mchnt_code_in=c.merc_id ");
		sql.append(" LEFT JOIN tbl_bo_eticket e ON t.host_trans_ssn=e.host_trans_ssn ");
		sql.append(" WHERE t.trans_status=0 and t.trans_code = '0200' ");
		
		if(null != query){
 			if(!StringUtils.isEmpty(query.getInscode())){
 				sql.append(" AND t.inscode = #{inscode} ");
			}
			
			if(!StringUtils.isEmpty(query.getHostTransSsn())){	//流水号
				sql.append(" AND t.host_trans_ssn LIKE '%" + query.getHostTransSsn() +"%' ");
			}
		}
		
		return sql.toString();
	}
}
