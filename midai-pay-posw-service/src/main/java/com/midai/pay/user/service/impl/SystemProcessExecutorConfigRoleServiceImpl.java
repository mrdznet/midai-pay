/**
 * Project Name:chenxun-framework-start
 * File Name:UserServiceImpl.java
 * Package Name:com.chenxun.framework.service.impl
 * Date:2016年8月31日下午5:36:59
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.user.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.user.entity.SystemProcessExecutorConfigRole;
import com.midai.pay.user.entity.SystemRoleModule;
import com.midai.pay.user.mapper.SystemProcessExecutorConfigRoleMapper;
import com.midai.pay.user.service.SystemProcessExecutorConfigRoleService;

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
public class SystemProcessExecutorConfigRoleServiceImpl extends BaseServiceImpl<SystemProcessExecutorConfigRole> implements SystemProcessExecutorConfigRoleService{
	
	private final SystemProcessExecutorConfigRoleMapper mapper;

    public SystemProcessExecutorConfigRoleServiceImpl(SystemProcessExecutorConfigRoleMapper mapper) {           
        super(mapper);  
        this.mapper=mapper;
    }

    @Transactional
	@Override
	public int insertList(List<SystemProcessExecutorConfigRole> surList) {
		Map<String, List<SystemProcessExecutorConfigRole>> map = new HashMap<String, List<SystemProcessExecutorConfigRole>>();
		map.put("list", surList);
		return mapper.insertBatch(map);
	}

    @Transactional
	@Override
	public int deleteByRoleId(String id) {
		Example example = new Example(SystemProcessExecutorConfigRole.class);
		example.createCriteria().andEqualTo("roleid", id);
		return mapper.deleteByExample(example);
	}

	@Override
	public List<SystemProcessExecutorConfigRole> getByRoleId(String id) {
		Example example = new Example(SystemProcessExecutorConfigRole.class);
		example.createCriteria().andEqualTo("roleid", id);
		return mapper.selectByExample(example);
	}

    
}

