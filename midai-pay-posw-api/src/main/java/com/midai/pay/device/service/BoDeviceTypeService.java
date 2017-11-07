package com.midai.pay.device.service;

import java.util.List;

import com.midai.framework.common.BaseService;
import com.midai.pay.common.po.ReturnVal;
import com.midai.pay.device.entity.BoDeviceType;
import com.midai.pay.device.query.BoDeviceTypeQuery;
import com.midai.pay.device.vo.DeviceTypeVo;

public interface BoDeviceTypeService extends BaseService<BoDeviceType> {
	
	/**
	 * 列表查询设备类型总数
	 * @param query
	 * @return
	 */
	public int queryCount();
	
	/**
	 * 列表查询设备类型
	 * @param query
	 * @return
	 */
	public List<DeviceTypeVo> queryList(BoDeviceTypeQuery query);
	
	/**
	 * 设备类型新增页面
	 * @return
	 */
	public DeviceTypeVo getInsertInfo();
	
	/**
	 * 设备类型新增
	 * @param deviceTypeVo
	 * @param userName
	 * @return
	 */
	public ReturnVal<String> insertDeviceType(DeviceTypeVo deviceTypeVo, String userName);
	
	/**
	 * 设备类型删除
	 * @param ids
	 * @param userName
	 * @return
	 */
	public ReturnVal<String> deleteDeviceType(Integer[] ids, String userName);

	public List<DeviceTypeVo> allDeviceType();
}
