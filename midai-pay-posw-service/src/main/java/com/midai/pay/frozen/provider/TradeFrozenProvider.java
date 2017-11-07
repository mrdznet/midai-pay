package com.midai.pay.frozen.provider;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.midai.pay.frozen.query.TradeFrozenQuery;
import com.midai.pay.frozen.vo.TradeFrozenVo;

public class TradeFrozenProvider {

	public String columns = " a.id id,a.host_trans_ssn hostTransssn,a.merc_id mercId,a.merc_name mercName,b.trans_amt/100 transAmt,b.trans_time transTime,"
			+ " b.trans_card_no transCardNo,a.frozen_time frozenTime,a.frozen_reason frozenReason,a.frozen_person frozenPerson,"
			+ " a.unfrozen_person unfrozenPerson,a.unfrozen_time unfrozenTime";

	/**
	 * 交易冻结管理查询
	 * 
	 * @param query
	 * @return
	 */

	public String findQueryMiIplaTradefrozenreason(TradeFrozenQuery query) {
		StringBuffer sql = new StringBuffer(" select " + columns + " from mi_ipla_tradefrozenreason a"
				+ " LEFT JOIN tbl_deal_total b ON a.host_trans_ssn=b.host_trans_ssn "  
				+ " WHERE 1=1  ");
		
		sql.append(this.comnStringBuffer(query));
		sql.append(" order by a.frozen_time DESC limit " + (query.getPageNumber() - 1) * query.getPageSize() + ","
				+ query.getPageSize());
		return sql.toString();
	}

	public String findQueryMiIplaTradefrozenreasonCount(TradeFrozenQuery query) {
		StringBuffer sql = new StringBuffer(" select count(1)  from mi_ipla_tradefrozenreason a"
				+ " LEFT JOIN tbl_deal_total b ON a.host_trans_ssn=b.host_trans_ssn "  
				+ " WHERE 1=1  ");
		sql.append(this.comnStringBuffer(query));
		return sql.toString();
	}
	

	/**
	 * 交易冻结管理查询Excel表格下载
	 * @param query
	 * @return
	 */
	public String ExcelDownMiIplaTradefrozenreason(TradeFrozenQuery query){
		StringBuffer sql=new StringBuffer(" select " + columns + " from mi_ipla_tradefrozenreason a"
				+ " LEFT JOIN tbl_deal_total b ON a.host_trans_ssn=b.host_trans_ssn "  
				+ " WHERE 1=1  ");
		
		sql.append(this.comnStringBuffer(query));
		return sql.toString();
	}
	
	public String ExcelDownMiIplaTradefrozenreasonCount(TradeFrozenQuery query){
		StringBuffer sql=new StringBuffer(" select count(1)  from mi_ipla_tradefrozenreason a"
				+ " LEFT JOIN tbl_deal_total b ON a.host_trans_ssn=b.host_trans_ssn "  
				+ " WHERE 1=1  ");
		sql.append(this.comnStringBuffer(query));
		return sql.toString();
		
	}
	
	/**
	 * 交易冻结管理查询Excel表格下载有复选框
	 * @param hosttransssns
	 * @return
	 */
     public String ExcelDownSelectMiIplaTradefrozenreason(String ids){
    	 StringBuffer sql=new StringBuffer(" select " + columns + " from mi_ipla_tradefrozenreason a"
				+ " LEFT JOIN tbl_deal_total b ON a.host_trans_ssn=b.host_trans_ssn "  
				+ " WHERE 1=1  ");
    	 
    	 if(StringUtils.isNotBlank(ids)){
    		 sql.append(" and a.id in("+ids+")");
    	 }
    	 
    	 return sql.toString();
    	 
     }
	/**
	 * 批量冻结插入数据
	 * @param frozenData
	 * @return
	 */
     public String frozenInsert(Map<String,List<TradeFrozenVo>> frozenData){
         List<TradeFrozenVo> list = frozenData.get("list");
         StringBuffer sql = new StringBuffer("insert into mi_ipla_tradefrozenreason(host_trans_ssn,merc_id,merc_name,frozen_time,frozen_person) values ");
         MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].hostTransssn},#'{'list[{0}].mercId},#'{'list[{0}].mercName},#'{'list[{0}].frozenTime},#'{'list[{0}].frozenPerson})");
         for (int i = 0; i < list.size(); i++) {
             sql.append(messageFormat.format(new Integer[]{i}));
             sql.append(",");
         }
         sql.setLength(sql.length() - 1);
         return sql.toString();
     }
	/**
	 * 批量解冻
	 * @param query
	 * @return
	 */
   public String unfrozenUpdate(Map<String, Object> map){
	String ids = (String)map.get("ids");
	/*Date unfrozenTime = (Date)map.get("unfrozenTime");
	String unfrozenPerson = (String)map.get("unfrozenPerson");*/
   	StringBuffer sql=new StringBuffer("update mi_ipla_tradefrozenreason set unfrozen_time= #{unfrozenTime},unfrozen_person=#{unfrozenPerson}  where id in (");
   sql.append(ids).append(")");
   	return sql.toString();
   }
     

	private String comnStringBuffer(TradeFrozenQuery query) {
		StringBuffer sql = new StringBuffer("");
		if (query != null) {
			
			if(!StringUtils.isEmpty(query.getInscode())){
				sql.append(" and b.inscode = #{inscode} ");
			}

			/* 商户小票号 */
			if (StringUtils.isNotBlank(query.getMercId())) {
				sql.append("and a.merc_id like '%" + query.getMercId() + "%' ");
			}
			/* 商户名称 */
			if (StringUtils.isNotBlank(query.getMercName())) {
				sql.append("and a.merc_name like '%" + query.getMercName() + "%' ");
			}
			/* 冻结日期始 */
			if (StringUtils.isNotBlank(query.getFrozenTimeBegin())) {
				sql.append("and date_format(a.frozen_time,'%Y-%m-%d') >= #{frozenTimeBegin}");
			}
			/* 冻结日期止 */
			if (StringUtils.isNotBlank(query.getFrozenTimeEnd())) {
				sql.append("and date_format(a.frozen_time,'%Y-%m-%d') <= #{frozenTimeEnd}");
			}
			/* 解冻日期始 */
			if (StringUtils.isNotBlank(query.getUnfrozenTimeBegin())) {
				sql.append("and date_format(a.unfrozen_time,'%Y-%m-%d') >= #{unfrozenTimeBegin}");
			}
			/* 解结日期止 */
			if (StringUtils.isNotBlank(query.getUnfrozenTimeEnd())) {
				sql.append("and date_format(a.unfrozen_time,'%Y-%m-%d') <= #{unfrozenTimeEnd}");
			}
			/* 流水号 */
			if (StringUtils.isNotBlank(query.getHostTransssn())) {
				sql.append("and a.host_trans_ssn like '%" + query.getHostTransssn() + "%' ");
			}
		}

		return sql.toString();
	}

}
