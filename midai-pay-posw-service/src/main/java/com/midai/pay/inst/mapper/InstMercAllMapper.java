package com.midai.pay.inst.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.midai.pay.inst.entity.InstMercAll;


public interface InstMercAllMapper {
	
	public String insert="inst_code,inst_merc_id,isst_merc_name,inst_device_id,merc_id,create_time";
	public String insertProperty="#{instCode},#{instMercId},#{isstMercName},#{instDeviceId},#{mercId},now()";
	
	@Insert(" insert into tbl_inst_merc("+insert+") values("+insertProperty+")")
    public int insertInstMercAll(InstMercAll instMercAll);
	
	@Select("select count(1) from tbl_inst_merc where inst_code=#{instCode} and merc_id=#{mercId}")
	public int selectInstMercAllCount(@Param("instCode")String instCode,@Param("mercId")String mercId);
	
	@Delete("delete from tbl_inst_merc where inst_code=#{instCode} and merc_id=#{mercId}")
	public int deleteInstMercAll(@Param("instCode")String instCode,@Param("mercId")String mercId);

	@Select("select inst_code instCode,inst_merc_id instMercId,isst_merc_name isstMercName,inst_device_id instDeviceId,merc_id mercId from tbl_inst_merc where inst_code=#{instCode} and merc_id=#{mercId}")
	public InstMercAll selectInstMerc(@Param("instCode")String instCode,@Param("mercId")String mercId);
}
