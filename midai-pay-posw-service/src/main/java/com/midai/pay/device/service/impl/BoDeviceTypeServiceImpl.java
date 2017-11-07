package com.midai.pay.device.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.common.constants.Constants;
import com.midai.pay.common.po.ReturnVal;
import com.midai.pay.device.entity.BoDeviceType;
import com.midai.pay.device.mapper.BoDeviceTypeMapper;
import com.midai.pay.device.query.BoDeviceTypeQuery;
import com.midai.pay.device.service.BoDeviceTypeService;
import com.midai.pay.device.vo.DeviceTypeVo;

@Service
public class BoDeviceTypeServiceImpl extends BaseServiceImpl<BoDeviceType> implements BoDeviceTypeService {

    private final BoDeviceTypeMapper mapper;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BoDeviceTypeServiceImpl.class);

    public BoDeviceTypeServiceImpl(BoDeviceTypeMapper mapper) {
        super(mapper);
        this.mapper=mapper;
    }

	@Override
	public int queryCount() {
		return  mapper.queryCount();
	}

	@Override
	public List<DeviceTypeVo> queryList(BoDeviceTypeQuery query) {
		List<DeviceTypeVo> deviceTypeVoList = new ArrayList<DeviceTypeVo>();
		List<BoDeviceType> deviceTypeList =  mapper.queryList(query);
		if(deviceTypeList == null) {
			deviceTypeList = new ArrayList<BoDeviceType>();
		}
		for (BoDeviceType deviceType : deviceTypeList) {
			DeviceTypeVo deviceTypeVo = new DeviceTypeVo();
			BeanUtils.copyProperties(deviceType, deviceTypeVo);
			deviceTypeVo.setStateName(deviceType.getState() != null ? Constants.DEVICE_TYPE_STATE_MAP.get(deviceType.getState()): null);
			deviceTypeVoList.add(deviceTypeVo);
		}
		return deviceTypeVoList;
	}

	@Override
	public DeviceTypeVo getInsertInfo() {
		return new DeviceTypeVo();
	}

	@Override
	@Transactional
	public ReturnVal<String> insertDeviceType(DeviceTypeVo deviceTypeVo, String userName) {
		BoDeviceType deviceType = new BoDeviceType();
		//vo和model数据转换
		BeanUtils.copyProperties(deviceTypeVo, deviceType);
		deviceType.setCreateTime(new Date());
		deviceType.setCreateUser(userName);
		deviceType.setUpdateUser(userName);
		mapper.insertSelective(deviceType);
		return ReturnVal.SUCCESS();
	}

	@Override
	@Transactional
	public ReturnVal<String> deleteDeviceType(Integer[] ids, String userName) {
		if(ids == null || ids.length <= 0) {
			LOGGER.info("删除设备类型参数为空！");
			throw new RuntimeException("{\"errorMsg\":\"删除设备类型参数为空！\"}");
		}
		StringBuilder idStr = new StringBuilder();
		for (Integer id : ids) {
			idStr.append("'").append(id).append("',");
		}
		idStr.setLength(idStr.length() - 1);
		mapper.updateState(idStr.toString(), Constants.DEVICE_TYPE_STATE_ABOLISH, userName);
		return ReturnVal.SUCCESS();
	}

	@Override
	public List<DeviceTypeVo> allDeviceType() {
		
		return mapper.allDeviceType();
	}
	
}
