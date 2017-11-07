package com.midai.pay.device.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.agent.entity.Agent;
import com.midai.pay.common.constants.Constants;
import com.midai.pay.common.po.ReturnVal;
import com.midai.pay.device.entity.BoDeviceMode;
import com.midai.pay.device.mapper.BoDeviceModeMapper;
import com.midai.pay.device.mapper.BoDeviceTypeMapper;
import com.midai.pay.device.query.BoDeviceModeQuery;
import com.midai.pay.device.service.BoDeviceModeService;
import com.midai.pay.device.vo.DeviceModeVo;

@Service
public class BoDeviceModeServiceImpl extends BaseServiceImpl<BoDeviceMode> implements BoDeviceModeService {

    private final BoDeviceModeMapper mapper;
    
    @Autowired
	private BoDeviceTypeMapper boDeviceTypeMapper;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BoDeviceModeServiceImpl.class);

    public BoDeviceModeServiceImpl(BoDeviceModeMapper mapper) {
        super(mapper);
        this.mapper=mapper;
    }

	@Override
	public int queryCount() {
		return  mapper.queryCount();
	}

	@Override
	public List<DeviceModeVo> queryList(BoDeviceModeQuery query) {
		List<DeviceModeVo> deviceModeVoList =  mapper.queryList(query);
		if(deviceModeVoList == null) {
			deviceModeVoList = new ArrayList<DeviceModeVo>();
		}
		for (DeviceModeVo deviceModeVo : deviceModeVoList) {
			deviceModeVo.setStateName(deviceModeVo.getState() != null ? Constants.DEVICE_MODE_STATE_MAP.get(deviceModeVo.getState()): null);
		}
		return deviceModeVoList;
	}

	@Override
	public DeviceModeVo getInsertInfo() {
		return new DeviceModeVo();
	}

	@Override
	@Transactional
	public ReturnVal<String> insertDeviceMode(DeviceModeVo deviceModeVo, String userName) {
		BoDeviceMode deviceMode = new BoDeviceMode();
		//vo和model数据转换
		BeanUtils.copyProperties(deviceModeVo, deviceMode);
		//获取设备类型id
		String name = "";
		if(deviceMode.getDeviceTypeId() != null && deviceMode.getDeviceTypeId().equals(Constants.DEVICE_TYPE_SKQ)) {
			name = Constants.DEVICE_TYPE_NAME_SKQ;
		} else if(deviceMode.getDeviceTypeId() != null && deviceMode.getDeviceTypeId().equals(Constants.DEVICE_TYPE_SKT)) {
			name = Constants.DEVICE_TYPE_NAME_SKT;
		}
		List<Integer> idList = boDeviceTypeMapper.getIdByName(name);
		if(idList != null && !idList.isEmpty()) {
			deviceMode.setDeviceTypeId(idList.get(0));
		}
		deviceMode.setCreateTime(new Date());
		deviceMode.setCreateUser(userName);
		deviceMode.setUpdateUser(userName);
		mapper.insertSelective(deviceMode);
		return ReturnVal.SUCCESS();
	}

	@Override
	@Transactional
	public ReturnVal<String> deleteDeviceMode(Integer[] ids, String userName) {
		if(ids == null || ids.length <= 0) {
			LOGGER.info("删除设备型号参数为空！");
			throw new RuntimeException("{\"errorMsg\":\"删除设备类型参数为空！\"}");
		}
		StringBuilder idStr = new StringBuilder();
		for (Integer id : ids) {
			idStr.append("'").append(id).append("',");
		}
		idStr.setLength(idStr.length() - 1);
		mapper.updateState(idStr.toString(), Constants.DEVICE_MODE_STATE_ABOLISH, userName);
		return ReturnVal.SUCCESS();
	}

	@Override
	public Agent getByDeviceNo(String deviceid) {
		
		return mapper.getByDeviceNo(deviceid);
	}
    
}
