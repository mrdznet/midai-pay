/**
 * Project Name:chenxun-framework-start
 * File Name:UserServiceImpl.java
 * Package Name:com.chenxun.framework.service.impl
 * Date:2016年8月31日下午5:36:59
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.user.service.impl;

import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.user.entity.SystemOrgRole;
import com.midai.pay.user.mapper.SystemOrgRoleMapper;
import com.midai.pay.user.service.SystemOrgRoleService;

import tk.mybatis.mapper.entity.Example;

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
@Service
public class SystemOrgRoleServiceImpl extends BaseServiceImpl<SystemOrgRole> implements SystemOrgRoleService{
	
	private final SystemOrgRoleMapper mapper;
	
	  public SystemOrgRoleServiceImpl(SystemOrgRoleMapper mapper) {           
	        super(mapper);  
	        this.mapper=mapper;
	    }
	  
	 @Transactional
	@Override
	public int deleteByRoleId(int id) {
		Example example = new Example(SystemOrgRole.class);
		example.createCriteria().andEqualTo("roleid", id);
		return mapper.deleteByExample(example);
	}
	

}

