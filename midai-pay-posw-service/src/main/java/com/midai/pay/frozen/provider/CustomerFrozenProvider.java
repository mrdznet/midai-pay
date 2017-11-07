package com.midai.pay.frozen.provider;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.midai.pay.frozen.query.CustomerFrozenQuery;
import com.midai.pay.frozen.vo.CustomerFrozenVo;

public class CustomerFrozenProvider {
	
	public String columns=" a.id id,a.merc_id mercId,a.merc_name mercName,a.frozen_time frozenTime,a.unfrozen_time unfrozenTime,"
			            + " a.frozen_reason frozenReason,a.frozen_person frozenPerson,a.unfrozen_person unfrozenPerson ";
	

	/**
	 * 商户冻结管理查询
	 * 
	 * @param query
	 * @return
	 */
	public String findQueryMiIplafrozenreason(CustomerFrozenQuery query){
		StringBuffer sql =new StringBuffer( " select " + columns + " FROM (SELECT c.id ,c.merc_id ,c.merc_name ,c.frozen_time ,c.unfrozen_time ,c.frozen_reason ,c.frozen_person ,c.unfrozen_person  FROM mi_ipla_frozenreason c GROUP BY c.frozen_time) AS a "
				+ " LEFT JOIN  tbl_bo_customer  b  on a.merc_id = b.merc_id  "
				+ " WHERE 1=1 ");
		
		sql.append(this.comnStringBuffer(query));
		
		sql.append("  order by a.frozen_time DESC limit " + (query.getPageNumber() - 1) * query.getPageSize() + ","
				+ query.getPageSize());
		return sql.toString();
	}
	
	public String findQueryMiIplafrozenreasonCount(CustomerFrozenQuery query){
	   StringBuffer sql=new StringBuffer(" select count(1) FROM (SELECT c.id ,c.merc_id ,c.merc_name ,c.frozen_time ,c.unfrozen_time ,c.frozen_reason ,c.frozen_person ,c.unfrozen_person  FROM mi_ipla_frozenreason c GROUP BY c.frozen_time) AS a "
	   		+ " LEFT JOIN  tbl_bo_customer  b  on a.merc_id = b.merc_id   "
				+ " WHERE 1=1 ");
	    sql.append(this.comnStringBuffer(query));
	    return sql.toString();
	}
	
	/**
	 * 商户冻结管理查询Excel表格下载
	 * @param query
	 * @return
	 */
	public String ExcelDownMiIplafrozenreason(CustomerFrozenQuery query){
		StringBuffer sql =new StringBuffer(" select " + columns + " FROM (SELECT c.id ,c.merc_id ,c.merc_name ,c.frozen_time ,c.unfrozen_time ,c.frozen_reason ,c.frozen_person ,c.unfrozen_person  FROM mi_ipla_frozenreason c GROUP BY c.frozen_time) AS a"
				+ " LEFT JOIN  tbl_bo_customer b on a.merc_id=b.merc_id "
				+ " WHERE 1=1 ");
		sql.append(this.comnStringBuffer(query));		
		return sql.toString();
		
	}
	
	public String ExcelDownMiIplafrozenreasonCount(CustomerFrozenQuery query){
		StringBuffer sql=new StringBuffer(" select count(1) FROM (SELECT c.id ,c.merc_id ,c.merc_name ,c.frozen_time ,c.unfrozen_time ,c.frozen_reason ,c.frozen_person ,c.unfrozen_person  FROM mi_ipla_frozenreason c GROUP BY c.frozen_time) AS a "
				+ " LEFT JOIN  tbl_bo_customer b on a.merc_id=b.merc_id "
				+ " WHERE 1=1 ");
	    sql.append(this.comnStringBuffer(query));
	    return sql.toString();
	}
	
	/**
	 * 交易冻结管理查询Excel表格下载有复选框
	 * @param hosttransssns
	 * @return
	 */
	public String ExcelDownSelectMiIplafrozenreason(String ids){
		StringBuffer sql =new StringBuffer(" select " + columns + " from mi_ipla_frozenreason a "
				+ " LEFT JOIN  tbl_bo_customer b on a.merc_id = b.merc_id "
				+ " WHERE 1=1 ");
		
		if(StringUtils.isNotBlank(ids)){
   		 sql.append(" and a.id in("+ids+")");
   	 }
		return sql.toString();
		
	}
	
	private String comnStringBuffer(CustomerFrozenQuery query){
	StringBuffer sql=new StringBuffer("");
	if(query !=null){
		
		
		if(!StringUtils.isEmpty(query.getInscode())){
			sql.append(" and b.inscode = #{inscode} ");
		}
		
        /*商户小票号*/
		if(StringUtils.isNotBlank(query.getMercId())){
         sql.append("and a.merc_id like '%" + query.getMercId() + "%' ");			
		}
		/*商户名称*/
		if(StringUtils.isNotBlank(query.getMercName())){
			sql.append("and a.merc_name like '%" + query.getMercName() + "%' ");
		}
		/*冻结日期始*/
		if(StringUtils.isNotBlank(query.getFrozenTimeBegin())){
			sql.append(" and date_format(a.frozen_time,'%Y-%m-%d') >= #{frozenTimeBegin}");
		}
		/*冻结日期止*/
		if(StringUtils.isNotBlank(query.getFrozenTimeEnd())){            
			sql.append(" and date_format(a.frozen_time,'%Y-%m-%d') <= #{frozenTimeEnd}");
		}
		/*解结日期始*/
		if(StringUtils.isNotBlank(query.getUnfrozenTimeBegin())){
			sql.append("and date_format(a.unfrozen_time,'%Y-%m-%d') >= #{unfrozenTimeBegin}");
		}
		/*解结日期止*/
        if(StringUtils.isNotBlank(query.getUnfrozenTimeEnd())){
        	sql.append("and date_format(a.unfrozen_time,'%Y-%m-%d') <= #{unfrozenTimeEnd}");
        }		
		
	}
	
	return sql.toString();
		
	}
	
	/*商户冻结插入数据*/
	 public String frozenInsert(Map<String,List<CustomerFrozenVo>> frozenData){
         List<CustomerFrozenVo> list = frozenData.get("list");
         StringBuffer sql = new StringBuffer("insert into mi_ipla_frozenreason(merc_id,merc_name,frozen_time,frozen_person,frozen_reason) values ");
         MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].mercId},#'{'list[{0}].mercName},#'{'list[{0}].frozenTime},#'{'list[{0}].frozenPerson},#'{'list[{0}].frozenReason})");
         for (int i = 0; i < list.size(); i++) {
             sql.append(messageFormat.format(new Integer[]{i}));
             sql.append(",");
         }
         sql.setLength(sql.length() - 1);
         return sql.toString();
     }
	 
	 /*商户冻结更新数据*/
	 public String unfrozenUpdate(Map<String, Object> map){
		String mercIds = (String)map.get("mercIds");
		/*Date unfrozenTime = (Date)map.get("unfrozenTime");
		String unfrozenPerson = (String)map.get("unfrozenPerson");*/
	   	StringBuffer sql=new StringBuffer("update mi_ipla_frozenreason set unfrozen_time= #{unfrozenTime},unfrozen_person=#{unfrozenPerson}  where merc_id in (");
	   	sql.append(mercIds).append(")");
	   	return sql.toString();
   }
	 
}
