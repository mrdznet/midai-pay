package com.midai.pay.device.service;

import java.util.List;

import com.midai.framework.common.BaseService;
import com.midai.framework.query.PageVo;
import com.midai.pay.device.entity.BoFactory;
import com.midai.pay.device.query.BoFactoryModeQuery;
import com.midai.pay.device.query.BoFactoryQuery;
import com.midai.pay.device.vo.DeviceFactoryModeVo;
import com.midai.pay.device.vo.FactoryVo;

public interface BoFactoryService extends BaseService<BoFactory> {

    public PageVo<DeviceFactoryModeVo> paginageAllDeviceMode(BoFactoryModeQuery query);
    
	int factoryCount(BoFactoryQuery query);
	
	List<FactoryVo> factoryQuery(BoFactoryQuery query);
	
	int add(FactoryVo vo);
	
	int del(String ids);
	
	public List<FactoryVo> queryFactoryInfo();
}
