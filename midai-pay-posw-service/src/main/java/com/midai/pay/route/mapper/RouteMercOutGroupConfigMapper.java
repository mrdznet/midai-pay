package com.midai.pay.route.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.route.entity.RouteMercInGroup;
import com.midai.pay.route.entity.RouteMercOutGroupConfig;

public interface RouteMercOutGroupConfigMapper extends MyMapper<RouteMercOutGroupConfig> {
	
	
	@Select("select id,name,rule_exp ruleExp,is_available isAvailable,remarks,create_time createTime,update_time updateTime from tbl_route_merc_out_group_config "
			+ "where id in ("
			+ "select out_id from tbl_route_merc_in_out "
			+ "where in_id in("
			+ "select id from tbl_route_merc_in_group_config "
			+ "where is_available = 1 and id in("
			+ "select in_config_id from tbl_route_merc_in_group "
			+ "where  unit_id =#{unitId}  and unit_type =#{unitType} )) order by priority desc)")
	List<RouteMercOutGroupConfig> selectByInGroup(RouteMercInGroup inGroup);
	
	
	@Select(" select max(id)+1 from tbl_route_merc_out_group_config ")
	long findNextMaxId();
	
	@Insert(" insert into tbl_route_merc_out_group_config(id,name,rule_exp,is_available,remarks) values(#{id},#{name},#{ruleExp},1,#{remarks})")
	long insertRouteMercOutGroupConfig(@Param("id")Integer id,@Param("name")String name,@Param("ruleExp")String ruleExp,@Param("remarks")String remarks);

}
