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
import com.midai.pay.user.entity.SystemProvince;
import com.midai.pay.user.mapper.SystemProvinceMapper;
import com.midai.pay.user.service.SystemProvinceService;

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
public class SystemProvinceServiceImpl extends BaseServiceImpl<SystemProvince> implements SystemProvinceService{
	
	private final SystemProvinceMapper mapper;

    public SystemProvinceServiceImpl(SystemProvinceMapper mapper) {           
        super(mapper);  
        this.mapper=mapper;
    }

	@Override
	public SystemProvince getByCode(String provinceId) {
		Example example = new Example(SystemProvince.class);
		example.createCriteria().andEqualTo("code", provinceId);
		List<SystemProvince> list = mapper.selectByExample(example);
		if(list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
    
}

