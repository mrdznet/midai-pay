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

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.user.entity.SystemRole;
import com.midai.pay.user.query.SystemCommNodeQuery;
import com.midai.pay.user.query.SystemRoleQuery;
import com.midai.pay.user.vo.SystemCommNodeVo;


/**
 * ClassName:User <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月13日  <br/>
 * @author   wrt
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
public interface SystemRoleMapper extends MyMapper<SystemRole> {

	
	@SelectProvider(type=com.midai.pay.user.provider.SystemRoleProvider.class , method="findByorgIds")
	List<SystemRole> findByorgIds(String orgIds);

	@Select("select * from tbl_system_role tr INNER JOIN tbl_system_organization_role  sr on tr.id=sr.roleid where sr.orgid=#{orgid} and tr.rolename=#{name}")
	List<SystemRole> getSystemRoleByNameAndOrgid(@Param("name") String name, @Param("orgid") Integer orgid);

	@Select("select * from tbl_system_role tr INNER JOIN tbl_system_organization_role  sr on tr.id=sr.roleid where sr.orgid=#{orgid}")
	List<SystemRole> getByOrgId(int organizationId);

	@SelectProvider(type=com.midai.pay.user.provider.SystemRoleProvider.class, method="queryRoleList")
	List<SystemCommNodeVo> queryRoleList(SystemCommNodeQuery query);

	@SelectProvider(type=com.midai.pay.user.provider.SystemRoleProvider.class, method="queryRoleCount")
	int queryRoleCount(SystemCommNodeQuery query);
	
	/*批量删除*/
	@DeleteProvider(type=com.midai.pay.user.provider.SystemRoleProvider.class,method="deleteSystemRole" )
	public int deleteSystemRole(String ids);
	
	@DeleteProvider(type=com.midai.pay.user.provider.SystemRoleProvider.class,method="deleteByRoleIds" )
	public int deleteByRoleIds(String ids);
	
	@DeleteProvider(type=com.midai.pay.user.provider.SystemRoleProvider.class,method="deleteByRoleids" )
	public int deleteByRoleids(String ids);
	
	@DeleteProvider(type=com.midai.pay.user.provider.SystemRoleProvider.class,method="deleteByRoleIdCs" )
	public int deleteByRoleIdCs(String ids);
	

}

