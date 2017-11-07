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

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.user.entity.SystemRole;
import com.midai.pay.user.entity.SystemUser;


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
public interface SystemUserMapper extends MyMapper<SystemUser> {

	@Update(" update tbl_system_user set status = 2 where id in (${ids})")
	int updateBatch(@Param("ids") String ids);
    
	@Select("select id Id,rolename roleName from tbl_system_role where id in(select roleid from tbl_system_user_role where userid=(select id from tbl_system_user where loginname=#{name} and status=1))")
	List<SystemRole> findVoByLoginName(String name);

	@Select(" select a.inscode from tbl_system_user u INNER JOIN tbl_system_organization o on u.orgid=o.organization_id "
			+ " inner join tbl_bo_agent a on o.agent_num=a.agent_no "
			+ " where u.loginname = #{userId}"
			+ " and u.status=1")
	String getAgentInscodeByUserId(String userId);

	@Select(" select * from tbl_system_user t where (t.loginname=#{mobile} or t.mobile=#{mobile}) and t.status=1 limit 1")
	SystemUser getByUserIdOrMobile(String mobile);

	@Update(" update tbl_system_user set loginname = #{newPhonenumber}, username=#{newPhonenumber}, mobile = #{newPhonenumber} where loginname = #{userId}")
	int updateLoginNameAndMobile(@Param("newPhonenumber")String newPhonenumber, @Param("userId")String userId);

	@Select(" SELECT inscode FROM tbl_system_user WHERE loginname=#{loginname} ")
	String getInscode(String loginname);

	@Update(" update tbl_system_user u set u.orgid=(select o.organization_id from tbl_system_organization o where o.agent_num=#{agentNo}) where u.loginname=#{mobile} ")
	int updateOrgIdByLoginName(@Param("mobile")String mobile, @Param("agentNo")String agentNo);
	
	@Select(" select inscode from tbl_system_organization where organization_name=#{organizationName} ")
	public String findInscode(String organizationName);
	
	@Select(" select organization_id from tbl_system_organization where organization_name=#{organizationName} ")
	public int findOrganizationId(String organizationName);
	
	@Select(" SELECT ba.agent_no FROM tbl_system_user su, tbl_bo_agent ba WHERE su.loginname=#{loginname} AND su.mobile=ba.mobile ")
	public String findCurrentAgentNo(String loginname);
	
	@Select("select count(1) from tbl_system_user where mobile=#{mobile}")
	public int selectBymobile(String mobile);
	
	@Update(" update tbl_system_user set mobile =#{mobile} where mobile = #{oldMobile}")
	int updateMobilebyLoginname(@Param("mobile")String mobile, @Param("oldMobile")String oldMobile);
	
	@Select(" SELECT loginname FROM tbl_system_user WHERE mobile=#{mobile} and inscode is not null ")
	public String getLoginNameByMobile(String mobile);
}

