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

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.user.entity.SystemArea;
import com.midai.pay.user.mapper.SystemAreaMapper;
import com.midai.pay.user.service.SystemAreaService;

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
public class SystemAreaServiceImpl extends BaseServiceImpl<SystemArea> implements SystemAreaService{
	
	private final SystemAreaMapper mapper;

    public SystemAreaServiceImpl(SystemAreaMapper mapper) {           
        super(mapper);  
        this.mapper=mapper;
    }

	@Override
	public List<SystemArea> findByProvinceCode(String pcode) {
		Example example = new Example(SystemArea.class);
		example.createCriteria().andEqualTo("father", pcode);
		mapper.selectByExample(example);
		return mapper.selectByExample(example);
	}
    
}

