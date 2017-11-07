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

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.user.entity.SystemUserRole;


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
public interface SystemUserRoleMapper extends MyMapper<SystemUserRole> {

	@InsertProvider(type=com.midai.pay.user.provider.SystemUserRoleProvider.class, method="insertBatch")
	int insertBatch(Map<String, List<SystemUserRole>> map);
    
	@Select(" SELECT ur.roleid FROM tbl_system_user_role ur LEFT JOIN tbl_system_user u ON ur.userid=u.id WHERE u.loginname=#{loginName} ")
	List<Integer> findAllRoleidByLoginName(String loginName);
	
	@Select(" SELECT u.loginname FROM tbl_system_user_role r, tbl_system_user u "
			+" WHERE u.`status`=1 AND r.userid=u.id "
			+"	AND r.roleid in (SELECT id FROM tbl_system_role tt WHERE tt.rolename=#{rolename}) ")
	List<String> findAllUserByRoleid(String rolename);
	
	@SelectProvider(type=com.midai.pay.user.provider.SystemUserRoleProvider.class, method="findUserByRoleAndInscode")
	List<String> findUserByRoleAndInscode(Map<String, String> paraMap);
}

