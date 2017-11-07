/**
 * Project Name:demo-dubbo-provider
 * File Name:UserMapper.java
 * Package Name:com.midai.demo.mapper
 * Date:2016年7月27日下午6:31:38
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.user.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.user.entity.SystemOrganizationModel;
import com.midai.pay.user.query.SystemCommNodeQuery;
import com.midai.pay.user.vo.SystemCommNodeVo;


/**
 * 
 * ClassName: SystemOrganizationMapper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2016年9月13日 下午3:23:08 <br/>
 *
 * @author 屈志刚
 * @version 
 * @since JDK 1.7
 */
public interface SystemOrganizationMapper extends MyMapper<SystemOrganizationModel> {
	
	public String colunm = "organization_id organizationId,parent_id parentId,organization_name organizationName,t_level tLevel,province_code provinceCode,province_name provinceName,city_code cityCode,city_name cityName,org_type orgType,agent_num agentNum";
	public String insert = "organization_id,parent_id,organization_name,t_level,province_code,province_name,city_code,city_name,org_type,agent_num,create_time";
	public String insertProperty = "#{organizationId},#{parentId},#{organizationName},#{level},#{provinceCode},#{provinceName},#{cityCode},#{cityName},#{orgType},#{agentNum},#{createTime}";
	
	@Insert("insert into tbl_system_organization ("+insert+") values ("+insertProperty+")")
	@SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="organizationId", before=false, resultType=Integer.class)
	int insertOrg(SystemOrganizationModel org);

	@Update("update tbl_system_organization set status=2 where organization_id in (${ids}) ")
	int updateThemselvesAndChildren(@Param("ids") String ids);

	@SelectProvider(type=com.midai.pay.user.provider.SystemOrganizationProvider.class, method="queryOrgAndUserList")
	List<SystemCommNodeVo> queryOrgAndUserList(SystemCommNodeQuery query);

	@SelectProvider(type=com.midai.pay.user.provider.SystemOrganizationProvider.class, method="queryOrgAndUserCount")
	int queryOrgAndUserCount(SystemCommNodeQuery query);
	
	@Update("update tbl_system_organization set organization_name = #{orgName} where agent_num = #{agentNo}")
	public Integer updateOrgNameByAgtNo(@Param("orgName") String orgName, @Param("agentNo") String agentNo);

	@SelectProvider(type=com.midai.pay.user.provider.SystemOrganizationProvider.class, method="getByParentIdAndNameAndStatus")
	List<SystemOrganizationModel> getByParentIdAndNameAndStatus(Map<String, String> map);
}

