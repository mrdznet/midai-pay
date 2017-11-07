package com.midai.pay.route.service;

import java.util.List;

import com.midai.pay.route.entity.RoleFileConfigEntity;

public interface RoleFileConfigService {

	/**
	 * 交易规则python文件
	 * @param 
	 * @return
	 */
	public List<RoleFileConfigEntity> getRoleFileConfigList(Integer instType);
}
