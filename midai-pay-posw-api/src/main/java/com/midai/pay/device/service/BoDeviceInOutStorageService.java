package com.midai.pay.device.service;

import java.util.List;

import com.midai.framework.common.BaseService;
import com.midai.pay.device.entity.BoDevice;
import com.midai.pay.device.query.BoDeviceInOutStorageQuery;
import com.midai.pay.device.vo.BoDeviceInOutStorageVo;




public interface BoDeviceInOutStorageService  extends BaseService<BoDevice>{

	
	/**
	 * 出入库明细查询			
	 * @param query
	 * @return
	 */
	public List<BoDeviceInOutStorageVo> findQueryBoDevice(BoDeviceInOutStorageQuery query);	
	
	public  int findQueryBoDeviceCount(BoDeviceInOutStorageQuery query);

	
	/**
	 * 出入库明细Excel下载			
	 * @param query
	 * @return
	 */	
	public List<BoDeviceInOutStorageVo> ExcelDownBoDevice(BoDeviceInOutStorageQuery query);
	
	public int ExcelDownloadBoDeviceCount(BoDeviceInOutStorageQuery query);
	
	
	/**
	 * 出入库明细查询Excel下载有复选框条件
	 * @param deviceNos
	 * @return
	 */
	public List<BoDeviceInOutStorageVo> ExcelDownSelectBoDevice(List<String> deviceNos);
	
	
	
	
	public List<BoDevice> findall();
	
	
}
