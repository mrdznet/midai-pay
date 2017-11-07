package com.midai.pay.route.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.route.entity.RouteMercInGroup;

public interface RouteMercInGroupMapper extends MyMapper<RouteMercInGroup> {

	@Insert(" insert into tbl_route_merc_in_group(in_config_id,unit_type,unit_id) values(#{inConfigId},#{unitType},#{unitId}) ")
	int insertRouteMercInGroup(@Param("inConfigId")int inConfigId,@Param("unitType")String unitType,@Param("unitId")String unitId);
}
