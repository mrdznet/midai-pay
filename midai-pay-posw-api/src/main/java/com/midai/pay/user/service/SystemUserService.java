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
import java.util.Map;

import com.midai.framework.common.BaseService;
import com.midai.pay.user.entity.SystemUser;
import com.midai.pay.user.vo.SystemUserSVo;

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
public interface SystemUserService extends BaseService<SystemUser>{
    

	public SystemUser loadByUserLoginname(String userId);

	public List<SystemUser> findByOrganizationId(String id);

	public int insertSystemUserVo(SystemUserSVo suv);

	public int delete(Integer id);

	public SystemUserSVo findSystemUserVo(Integer id);

	public int update(SystemUserSVo suv);
	
	/**
	 * 查询登陆用户信息，忽略是否被删除
	 * @param loginname
	 * @return
	 */
	public SystemUser getByLoginnameIgnoreStatus(String userId);
	/**
	 * 用户是否重复	true 存在， false 不存在
	 * @param loginname
	 * @return
	 */
	public boolean checkExistsUser(String userId, Integer id);

	public int updatePassword(SystemUser su);

	public int updateBatch(String ids);
	
	/**
	 * 根据登录名查询角色名称
	 */
	public SystemUserSVo findVoByLoginName(String name);

	public SystemUser getByLoginNameAndStatus(String userId, Integer status);

	public int updateOnlyUserInfo(SystemUser user);

	public String getAgentInscodeByUserId(String userId);

	/**
	 * 功能：根据用户手机号查找用户信息
	 * 条件：登陆名或手机号码相等
	 * @param eXTEND1 手机号码
	 * @return
	 */
	public SystemUser getByUserIdOrMobile(String eXTEND1);

	
	public String getInscode(String loginname);
	
	public Map<String, Map<String, List<String>>> findAllResource(String loginName);
	
	public String getAllChildAgents(String loginname);
	
	public int selectBymobile(String mobile);
}

