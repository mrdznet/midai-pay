/**
 * Project Name:midai-activiti-demo
 * File Name:CustomGroupManager.java
 * Package Name:com.midai.activiti.service.impl
 * Date:2016年5月16日上午9:55:10
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.activiti.service.impl;

import java.util.List;
import java.util.Map;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.persistence.AbstractManager;

import com.midai.pay.activiti.service.CustomGroupManager;


/**
 * ClassName:CustomGroupManager <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年5月16日 上午9:55:10 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */

public class CustomGroupManagerImpl extends AbstractManager implements CustomGroupManager {

	@Override
	public Group createNewGroup(String groupId) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertGroup(Group group) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateGroup(Group updatedGroup) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteGroup(String groupId) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public GroupQuery createNewGroupQuery() {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Group> findGroupByQueryCriteria(GroupQueryImpl query, Page page) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long findGroupCountByQueryCriteria(GroupQueryImpl query) {
		
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Group> findGroupsByUser(String userId) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Group> findGroupsByNativeQuery(
			Map<String, Object> parameterMap, int firstResult, int maxResults) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long findGroupCountByNativeQuery(Map<String, Object> parameterMap) {
		
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isNewGroup(Group group) {
		
		// TODO Auto-generated method stub
		return false;
	}



	
	

	
}

