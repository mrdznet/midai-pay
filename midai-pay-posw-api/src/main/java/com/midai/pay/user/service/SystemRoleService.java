/**
 * Project Name:chenxun-framework-start
 * File Name:UserService.java
 * Package Name:com.chenxun.framework.service
 * Date:2016年8月31日下午5:37:10
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.user.service;

import java.util.List;

import com.midai.framework.common.BaseService;
import com.midai.pay.user.entity.SystemRole;
import com.midai.pay.user.query.SystemCommNodeQuery;
import com.midai.pay.user.vo.SystemCommNodeVo;
import com.midai.pay.user.vo.SystemRoleSVo;

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
public interface SystemRoleService extends BaseService<SystemRole>{

	int save(SystemRoleSVo rv);

	List<SystemRole> list(int orgid);

	int delete(int id);
	
	/*批量删除*/
	int deletes(Integer[] ids);

	List<SystemCommNodeVo> loadNodes(int orgid);

	SystemRoleSVo getSystemRoleVo(int roleid);

	int updateSystemRoleVo(SystemRoleSVo srv);

	int updateBySystemRoleVo(SystemRoleSVo srv);

	SystemRole getSystemRoleByName(String agentRole);

	SystemRole getSystemRoleByNameAndOrgid(String agentRole, Integer orgid);
	
	boolean checkExists(String name, Integer orgId, Integer Id);

	List<SystemCommNodeVo> loadAll();

	List<SystemCommNodeVo> queryRoleList(SystemCommNodeQuery query);

	int queryRoleCount(SystemCommNodeQuery query);
}

