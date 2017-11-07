package com.midai.pay.inst.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.midai.pay.inst.entity.InstAll;
import com.midai.pay.inst.query.InstAllQuery;

public interface InstAllMapper {

	public String insert="inst_code,inst_name,key_mode,tpdu,msg_head,inst_ip,inst_port,inst_fee_rate,inst_fee_max,inst_dfree_rate,create_time,"
			+ "inst_terminal_number,inst_card_type,inst_route_support,inst_platform_ratio,inst_min_cost,inst_t0_factorage,inst_d1_factorage,inst_limit,inst_state,inst_factorage_state,inst_factorage_max";
	
	public String insertProperty="#{instCode},#{instName},#{keyMode},#{tpdu},#{msgHead},#{instIp},#{instPort},#{instFeeRate},#{instFeeMax},#{instDfreeRate},now(),"
			+ "#{instTerminalNumber},#{instCardType},#{instRouteSupport},#{instPlatformRatio},#{instMinCost},#{instT0Factorage},#{instD1Factorage},#{instLimit},#{instState},#{instFactorageState},#{instFactorageMax}";
    
	  @Insert(" insert into tbl_inst("+insert+") values("+insertProperty+")")
	    public int insertInstAll(InstAll instAll);
	    
	  @Select("select count(1) from tbl_inst where inst_code=#{instCode}")
	    public int selectInstAllCount(String instCode);

	  @Update("update tbl_inst set inst_state=#{instState} where inst_code in ( ${idStr})")
	    public int batchUpdateInst(@Param("idStr")String idStr,@Param("instState")int instState);
	  
	  @SelectProvider(type=com.midai.pay.inst.provider.InstAllProvider.class, method="instAllList")
	    public List<InstAll> instAllList(InstAllQuery query);
	  
	  @SelectProvider(type=com.midai.pay.inst.provider.InstAllProvider.class, method="instAllListCount")
	    public int instAllListCount(InstAllQuery query);
	  
	  @UpdateProvider(type=com.midai.pay.inst.provider.InstAllProvider.class,method="updateInstAll")
		public int updateInstAll(InstAll instAll);
	  
	  public String columns="inst_code instCode,inst_name instName,key_mode keyMode,tpdu,msg_head msgHead,inst_ip instIp,inst_port instPort,inst_fee_rate instFeeRate,inst_fee_max instFeeMax,inst_dfree_rate instDfreeRate,create_time createTime,update_time updateTime,"
				+ "inst_terminal_number instTerminalNumber,inst_card_type instCardType,inst_route_support instRouteSupport,inst_platform_ratio instPlatformRatio,inst_min_cost instMinCost,inst_t0_factorage instT0Factorage,inst_d1_factorage instD1Factorage,"
				+ "inst_limit instLimit,inst_state instState,inst_factorage_state instFactorageState,inst_factorage_max instFactorageMax";
		
	  @Select("select "+columns+" from tbl_inst where inst_state=0")
	  public List<InstAll> findAll();
	  
	  @Select("select "+columns+" from tbl_inst where inst_code=#{instCode}")
	  public InstAll selectInstAllById(String instCode);
	  
}
