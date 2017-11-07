package com.midai.pay.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.device.entity.BoDevice;
import com.midai.pay.device.query.BoDeviceInOutStorageQuery;
import com.midai.pay.device.vo.BoDeviceInOutStorageVo; 

public interface BoDeviceInOutStorageMapper extends MyMapper<BoDevice> {

	
	@SelectProvider(type=com.midai.pay.device.provider.BoDeviceInOutStorageProvider.class,method="findQueryBoDevice")
	public List<BoDeviceInOutStorageVo> findQueryBoDevice (BoDeviceInOutStorageQuery query);


	@SelectProvider(type=com.midai.pay.device.provider.BoDeviceInOutStorageProvider.class,method="findQueryBoDeviceCount")
	public int findQueryBoDeviceCount (BoDeviceInOutStorageQuery query);

	@SelectProvider(type=com.midai.pay.device.provider.BoDeviceInOutStorageProvider.class,method="ExcelDownBoDevice")
	public List<BoDeviceInOutStorageVo> ExcelDownBoDevice (BoDeviceInOutStorageQuery query);


	@SelectProvider(type=com.midai.pay.device.provider.BoDeviceInOutStorageProvider.class,method="ExcelDownloadBoDeviceCount")
	public int ExcelDownloadBoDeviceCount (BoDeviceInOutStorageQuery query);
	
	/*支持Excel下载有复选框*/
	@SelectProvider(type=com.midai.pay.device.provider.BoDeviceInOutStorageProvider.class,method="ExcelDownSelectBoDevice")
	public List<BoDeviceInOutStorageVo> ExcelDownSelectBoDevice (String deviceNos);

	

}
