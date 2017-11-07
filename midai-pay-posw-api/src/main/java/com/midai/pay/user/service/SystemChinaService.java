package com.midai.pay.user.service;

import java.util.List;

import com.midai.framework.common.BaseService;
import com.midai.pay.user.entity.SystemChina;

public interface SystemChinaService extends BaseService<SystemChina> {
	
	List<SystemChina> loadAllData(String code, String type);
	
	SystemChina findByCode(String code);
}
