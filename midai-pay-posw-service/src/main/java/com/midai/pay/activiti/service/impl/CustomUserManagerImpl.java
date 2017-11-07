/**
 * Project Name:midai-activiti-demo
 * File Name:CustomUserManager.java
 * Package Name:com.midai.activiti.service.impl
 * Date:2016年5月16日上午10:06:07
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
 */

package com.midai.pay.activiti.service.impl;

import java.util.List;
import java.util.Map;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.persistence.AbstractManager;
import org.activiti.engine.impl.persistence.entity.IdentityInfoEntity;

import com.midai.pay.activiti.service.CustomUserManager;


/**
 * ClassName:CustomUserManager <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年5月16日 上午10:06:07 <br/>
 * 
 * @author 陈勋
 * @version
 * @since JDK 1.7
 * @see
 */
public class CustomUserManagerImpl extends AbstractManager implements
		CustomUserManager {

	@Override
	public User createNewUser(String userId) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertUser(User user) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUser(User updatedUser) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public User findUserById(String userId) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUser(String userId) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<User> findUserByQueryCriteria(UserQueryImpl query, Page page) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long findUserCountByQueryCriteria(UserQueryImpl query) {
		
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Group> findGroupsByUser(String userId) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserQuery createNewUserQuery() {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IdentityInfoEntity findUserInfoByUserIdAndKey(String userId,
			String key) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> findUserInfoKeysByUserIdAndType(String userId,
			String type) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean checkPassword(String userId, String password) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> findPotentialStarterUsers(String proceDefId) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> findUsersByNativeQuery(Map<String, Object> parameterMap,
			int firstResult, int maxResults) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long findUserCountByNativeQuery(Map<String, Object> parameterMap) {
		
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isNewUser(User user) {
		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Picture getUserPicture(String userId) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUserPicture(String userId, Picture picture) {
		
		// TODO Auto-generated method stub
		
	}

}
