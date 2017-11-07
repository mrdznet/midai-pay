package com.midai.pay.route.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.route.entity.RouteMercInGroupConfig;

public interface RouteMercInGroupConfigMapper extends MyMapper<RouteMercInGroupConfig> {
	@Select(" select max(id)+1 from tbl_route_merc_in_group_config ")
	long findNextMaxId();
	
	@Insert(" insert into tbl_route_merc_in_group_config(id,name,is_available,remarks) values(#{id},#{name},1,#{remarks})")
	int insertRouteMercIn(@Param("id")Integer id,@Param("name")String name,@Param("remarks")String remarks);
}
