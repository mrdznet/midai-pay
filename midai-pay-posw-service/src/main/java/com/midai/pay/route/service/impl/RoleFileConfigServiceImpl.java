package com.midai.pay.route.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.route.entity.RoleFileConfigEntity;
import com.midai.pay.route.mapper.RoleFileConfigMapper;
import com.midai.pay.route.service.RoleFileConfigService;

import tk.mybatis.mapper.entity.Example;

@Service
public class RoleFileConfigServiceImpl  implements RoleFileConfigService {

	@Autowired
	private RoleFileConfigMapper mapper;
	
	@Override
	public List<RoleFileConfigEntity> getRoleFileConfigList(Integer instType) {
		List<Integer> param = new ArrayList<Integer>();
		param.add(0);
		if(instType != null && instType != 0) {
			param.add(instType);
		}
		
		Example example = new Example(RoleFileConfigEntity.class);
		example.createCriteria().andEqualTo("status", 1).andIn("instType", param);
		
		return mapper.selectByExample(example);
	}

}
