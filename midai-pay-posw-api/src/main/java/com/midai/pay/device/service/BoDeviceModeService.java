package com.midai.pay.device.service;

import java.util.List;

import com.midai.framework.common.BaseService;
import com.midai.pay.agent.entity.Agent;
import com.midai.pay.common.po.ReturnVal;
import com.midai.pay.device.entity.BoDeviceMode;
import com.midai.pay.device.query.BoDeviceModeQuery;
import com.midai.pay.device.vo.DeviceModeVo;

public interface BoDeviceModeService extends BaseService<BoDeviceMode> {

	/**
	 * 列表查询设备型号总数
	 * @param query
	 * @return
	 */
	public int queryCount();
	
	/**
	 * 列表查询设备型号
	 * @param query
	 * @return
	 */
	public List<DeviceModeVo> queryList(BoDeviceModeQuery query);
	
	/**
	 * 设备型号新增页面
	 * @return
	 */
	public DeviceModeVo getInsertInfo();
	
	/**
	 * 设备型号新增
	 * @param deviceTypeVo
	 * @param userName
	 * @return
	 */
	public ReturnVal<String> insertDeviceMode(DeviceModeVo deviceModeVo, String userName);
	
	/**
	 * 设备型号删除
	 * @param ids
	 * @param userName
	 * @return
	 */
	public ReturnVal<String> deleteDeviceMode(Integer[] ids, String userName);

	public Agent getByDeviceNo(String deviceid);
	
}
