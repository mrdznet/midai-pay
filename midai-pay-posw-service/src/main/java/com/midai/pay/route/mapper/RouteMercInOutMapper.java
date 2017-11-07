package com.midai.pay.route.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.route.entity.RouteMercInOut;

public interface RouteMercInOutMapper extends MyMapper<RouteMercInOut> {

	@Insert(" insert into tbl_route_merc_in_out(in_id,out_id,priority) values(#{inId},#{outId},1)")
	int insertRouteMercInOut(@Param("inId") Integer inId,@Param("outId")Integer outId);
}
