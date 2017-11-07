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
public interface SystemUserRoleService extends BaseService<SystemUserRole>{

	int insertList(List<SystemUserRole> surList);

	int deleteByUserId(Integer id);

	List<SystemUserRole> findByUserId(Integer id);
    

}

