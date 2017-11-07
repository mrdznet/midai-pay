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
import com.midai.pay.user.entity.SystemUserRole;
import com.midai.pay.user.mapper.SystemUserRoleMapper;
import com.midai.pay.user.service.SystemUserRoleService;

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
public class SystemUserRoleServiceImpl extends BaseServiceImpl<SystemUserRole> implements SystemUserRoleService{
	
	private final SystemUserRoleMapper mapper;

    public SystemUserRoleServiceImpl(SystemUserRoleMapper mapper) {           
        super(mapper);  
        this.mapper=mapper;
    }

    @Transactional
	@Override
	public int insertList(List<SystemUserRole> surList) {
		Map<String, List<SystemUserRole>> map = new HashMap<String, List<SystemUserRole>>();
		map.put("list", surList);
		return mapper.insertBatch(map);
	}

    @Transactional
	@Override
	public int deleteByUserId(Integer id) {
		Example example = new Example(SystemUserRole.class);
		example.createCriteria().andEqualTo("userid", id);
		return mapper.deleteByExample(example);
	}

	@Override
	public List<SystemUserRole> findByUserId(Integer id) {
		Example example = new Example(SystemUserRole.class);
		example.createCriteria().andEqualTo("userid", id);
		return mapper.selectByExample(example);
	}
    
   
}

