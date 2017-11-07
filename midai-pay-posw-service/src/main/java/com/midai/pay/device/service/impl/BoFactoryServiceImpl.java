package com.midai.pay.device.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.framework.query.PageVo;
import com.midai.pay.device.entity.BoFactory;
import com.midai.pay.device.mapper.BoFactoryMapper;
import com.midai.pay.device.query.BoFactoryModeQuery;
import com.midai.pay.device.query.BoFactoryQuery;
import com.midai.pay.device.service.BoFactoryService;
import com.midai.pay.device.vo.DeviceFactoryModeVo;
import com.midai.pay.device.vo.FactoryVo;

@Service
public class BoFactoryServiceImpl extends BaseServiceImpl<BoFactory> implements BoFactoryService {

    private final BoFactoryMapper boFactoryMapper;

    public BoFactoryServiceImpl(BoFactoryMapper mapper) {
        super(mapper);
        this.boFactoryMapper=mapper;
    }

    @Override
    public PageVo<DeviceFactoryModeVo> paginageAllDeviceMode(BoFactoryModeQuery query) {
        PageVo<DeviceFactoryModeVo> result=new PageVo<DeviceFactoryModeVo>();
        result.setRows(boFactoryMapper.paginageAllDeviceMode(query));
        result.setTotal(boFactoryMapper.paginageAllDeviceModeCount(query));
        return result;
    }

	@Override
	public int factoryCount(BoFactoryQuery query) {
		return boFactoryMapper.factoryCount(query);
	}

	@Override
	public List<FactoryVo> factoryQuery(BoFactoryQuery query) {
		return boFactoryMapper.factoryQuery(query);
	}

	@Override
	public int add(FactoryVo vo) {
		BoFactory bo = new BoFactory();
		BeanUtils.copyProperties(vo, bo);
		bo.setCreateTime(new Date());
		return boFactoryMapper.insert(bo);
	}

	@Override
	public int del(String ids) {
		String[] idArr = ids.split(",");
		for(String id : idArr){
			boFactoryMapper.updateState(Integer.valueOf(id));
		}
		
		return 1;
	}

	@Override
	public List<FactoryVo> queryFactoryInfo() {
		List<FactoryVo> factoryVoList = boFactoryMapper.queryFactoryInfo();
		if(factoryVoList == null) {
			factoryVoList = new ArrayList<FactoryVo>();
		}
		return factoryVoList;
	}
	
}
