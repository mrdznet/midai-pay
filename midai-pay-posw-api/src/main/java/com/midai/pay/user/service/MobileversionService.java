package com.midai.pay.user.service;

import com.midai.framework.common.BaseService;
import com.midai.pay.user.entity.Mobileversion;

public interface MobileversionService extends BaseService<Mobileversion> {

	Mobileversion getLastVersion(String device, Integer type);
	
}
