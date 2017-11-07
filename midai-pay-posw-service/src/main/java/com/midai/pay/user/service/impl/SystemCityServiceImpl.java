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
import com.midai.pay.user.entity.SystemCity;
import com.midai.pay.user.mapper.SystemCityMapper;
import com.midai.pay.user.service.SystemCityService;

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
public class SystemCityServiceImpl extends BaseServiceImpl<SystemCity> implements SystemCityService{
	
	private final SystemCityMapper mapper;

    public SystemCityServiceImpl(SystemCityMapper mapper) {           
        super(mapper);  
        this.mapper=mapper;
    }

	@Override
	public List<SystemCity> findByProvinceCode(String pcode) {
		Example example = new Example(SystemCity.class);
		example.createCriteria().andEqualTo("father", pcode);
		mapper.selectByExample(example);
		return mapper.selectByExample(example);
	}

	@Override
	public SystemCity findByCode(String code) {
		return mapper.findByCode(code);
	}
    
}

