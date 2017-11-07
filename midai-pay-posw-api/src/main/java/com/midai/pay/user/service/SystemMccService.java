package com.midai.pay.user.service;

import java.util.List;

import com.midai.framework.common.BaseService;
import com.midai.pay.user.entity.SystemMCC;

public interface SystemMccService extends BaseService<SystemMCC> {
	
	List<SystemMCC> loadData(String code);
	
	SystemMCC loadById(int id);
}
