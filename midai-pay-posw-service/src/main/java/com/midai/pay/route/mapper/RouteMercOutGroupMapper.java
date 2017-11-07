package com.midai.pay.route.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.route.entity.RouteMercOutGroup;

public interface RouteMercOutGroupMapper extends MyMapper<RouteMercOutGroup> {
	
	@Select("select out_config_id outConfigId,merc_out_id mercOutId,create_time createTime,update_time updateTime from tbl_route_merc_out_group where out_config_id "
			+ "in(select id from tbl_route_merc_out_group_config where "
			+ "is_available = 1 and id =#{id})")
	List<RouteMercOutGroup> selectByOutConfigId(int id);

	@Insert(" insert into tbl_route_merc_out_group(out_config_id,merc_out_id) values(#{outConfigId},#{mercOutId})")
	int insertRouteMercOut(@Param("outConfigId")int outConfigId,@Param("mercOutId")String mercOutId);
	
	@Select("select inst_code from tbl_inst where inst_state=0")
	List<String> selectAllOpenInst();
	
	@Select("select DISTINCT inst_merc_id from tbl_inst_merc where inst_code=#{instCode} and merc_id=#{mercId} ")
	String selectInst(@Param("instCode")String instCode, @Param("mercId")String mercId);
}
