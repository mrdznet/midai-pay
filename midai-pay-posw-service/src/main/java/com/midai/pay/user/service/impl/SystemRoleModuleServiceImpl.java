/**
 * Project Name:chenxun-framework-start
 * File Name:UserServiceImpl.java
 * Package Name:com.chenxun.framework.service.impl
 * Date:2016年8月31日下午5:36:59
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.user.service.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.user.entity.SystemRoleModule;
import com.midai.pay.user.mapper.SystemRoleModuleMapper;
import com.midai.pay.user.service.SystemRoleModuleService;

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
public class SystemRoleModuleServiceImpl extends BaseServiceImpl<SystemRoleModule> implements SystemRoleModuleService{
	
	private final SystemRoleModuleMapper mapper;

    public SystemRoleModuleServiceImpl(SystemRoleModuleMapper mapper) {           
        super(mapper);  
        this.mapper=mapper;
    }

    @Override
    public List<SystemRoleModule> getByRoleId(Integer roleid) {
    	
    	Example example = new Example(SystemRoleModule.class);
		example.createCriteria().andEqualTo("roleId", roleid);
    	return mapper.selectByExample(example);
    }

    @Transactional
	@Override
	public int insertList(List<SystemRoleModule> srmList) {
		return mapper.insertList(srmList);
	}

    @Transactional
	@Override
	public int deleteByRoleid(int roleid) {
		Example example = new Example(SystemRoleModule.class);
		example.createCriteria().andEqualTo("roleId", roleid);
		return mapper.deleteByExample(example);
	}
    
}

