package com.midai.pay.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.user.entity.SystemChina;
import com.midai.pay.user.mapper.SystemChinaMapper;
import com.midai.pay.user.service.SystemChinaService;

import tk.mybatis.mapper.entity.Example;

@Service
public class SystemChinaServiceImpl extends BaseServiceImpl<SystemChina> implements SystemChinaService {
	
	private final SystemChinaMapper mapper;
	
	public SystemChinaServiceImpl(SystemChinaMapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}


	@Override
	public List<SystemChina> loadAllData(String code, String type) {
		List<SystemChina> list = new ArrayList<SystemChina>();
		SystemChina one = new SystemChina();
		
		if(type.equals("1")){ //加载市,县
			switch (code) {
			case "1000":	//北京市
				one.setName("北京市");
				one.setCode(code);
				list.add(one);
				break;
			case "1100":	//天津市
				one.setName("天津市");
				one.setCode(code);
				list.add(one);
				break;
			case "2900":	//上海市
				one.setName("上海市");
				one.setCode(code);
				list.add(one);
				break;
			case "6900":  // 重庆市
				one.setName("重庆市");
				one.setCode(code);
				list.add(one);
				break;
			default:
				list = this.loadDate(code);
				break;
			}
		}else{	//加载省
			list = this.loadDate(code);
		}
		
		return list;
	}
	
	List<SystemChina> loadDate(String code) {
		Example ex = new Example(SystemChina.class);
		ex.createCriteria().andEqualTo("father", code);
		
		return mapper.selectByExample(ex);
	}


	@Override
	public SystemChina findByCode(String code) {
		Example ex = new Example(SystemChina.class);
		ex.createCriteria().andEqualTo("code", code);
		return mapper.selectByExample(ex).get(0);
	}
	
}
