package com.midai.pay.inst.provider;

import org.springframework.util.StringUtils;

import com.midai.pay.inst.entity.InstAll;
import com.midai.pay.inst.query.InstAllQuery;

public class InstAllProvider {
	
	public String columns="inst_code instCode,inst_name instName,key_mode keyMode,tpdu,msg_head msgHead,inst_ip instIp,inst_port instPort,inst_fee_rate instFeeRate,inst_fee_max instFeeMax,inst_dfree_rate instDfreeRate,create_time createTime,update_time updateTime,"
			+ "inst_terminal_number instTerminalNumber,inst_card_type instCardType,inst_route_support instRouteSupport,inst_platform_ratio instPlatformRatio,inst_min_cost instMinCost,inst_t0_factorage instT0Factorage,inst_d1_factorage instD1Factorage,"
			+ "inst_limit instLimit,inst_state instState,inst_factorage_state instFactorageState,inst_factorage_max instFactorageMax";
	
	public String instAllListCount(InstAllQuery query){
		StringBuffer sql = new StringBuffer(" SELECT COUNT(1) FROM tbl_inst WHERE 1=1  ");
		
		if(null != query){
			if(!StringUtils.isEmpty(query.getInstCode())){
				sql.append(" AND inst_code = #{instCode} ");
			}
			
			if(!StringUtils.isEmpty(query.getInstName())){	
				sql.append(" AND inst_name LIKE '%" + query.getInstName() + "%'  ");
			}
			
			if(!StringUtils.isEmpty(query.getInstState())&& query.getInstState()!=-1){	
				sql.append(" AND inst_state = #{instState} ");	
			}
			
			if(!StringUtils.isEmpty(query.getServiceType())&& query.getInstState() > 0){	
				sql.append(" AND service_type != #{serviceType} ");	
			}
		
		}
		
		return sql.toString();
	}

	public String instAllList(InstAllQuery query){
		StringBuffer sql = new StringBuffer(" SELECT  "+ columns +" FROM tbl_inst WHERE 1=1  ");
		
		if(null != query){
			if(!StringUtils.isEmpty(query.getInstCode())){
				sql.append(" AND inst_code = #{instCode} ");
			}
			
			if(!StringUtils.isEmpty(query.getInstName())){	
				sql.append(" AND inst_name LIKE '%" + query.getInstName() + "%'  ");
			}
			
			if(!StringUtils.isEmpty(query.getInstState())&& query.getInstState()!=-1){	
				sql.append(" AND inst_state = #{instState} ");	
			}
			
			if(!StringUtils.isEmpty(query.getServiceType())&& query.getInstState() > 0){	
				sql.append(" AND service_type != #{serviceType} ");	
			}
		
		}
		sql.append(" order by create_time DESC ");
		
		if (query.getPageSize() > 0) {
			sql.append(" limit " + (query.getPageNumber()-1)*query.getPageSize()+","+query.getPageSize());
		}
		
		
		return sql.toString();
	}
	
	public String updates="inst_code=#{instCode},inst_name=#{instName},key_mode=#{keyMode},tpdu=#{tpdu},msg_head=#{msgHead},inst_ip=#{instIp},inst_port=#{instPort},inst_fee_rate=#{instFeeRate},"
			+ "inst_fee_max=#{instFeeMax},inst_dfree_rate=#{instDfreeRate},inst_terminal_number=#{instTerminalNumber},inst_card_type=#{instCardType},inst_route_support=#{instRouteSupport},"
			+ "inst_platform_ratio=#{instPlatformRatio},inst_min_cost=#{instMinCost},inst_t0_factorage=#{instT0Factorage},inst_d1_factorage=#{instD1Factorage},inst_limit=#{instLimit},"
			+ "inst_state=#{instState},inst_factorage_state=#{instFactorageState},inst_factorage_max=#{instFactorageMax}";
	
	public String updateInstAll(InstAll instAll){
		 StringBuffer sql=new StringBuffer("update tbl_inst set "+updates+" where inst_code=#{instCode}");
    	 return sql.toString();
	}
}
