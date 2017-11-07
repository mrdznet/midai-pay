package com.midai.pay.customer.service;

import java.util.List;

import com.midai.framework.common.BaseService;
import com.midai.pay.customer.entity.BoCustomerDevice;
import com.midai.pay.device.vo.DeviceDetailVo;

public interface BoCustomerDeviceService extends BaseService<BoCustomerDevice> {
	
	//设备信息表的新增  (传入参数的是机身号和商户编号)
	public DeviceDetailVo insert(String bodyNo,String mercNo,int isFirst,String typeName,String modeName,int id);
	//设备信息表的批量删除 (传入单个或者多个机身号)
	public int batchDelete(String mercNo,String[] bodyNos,int id);
	/**
	 * 根据商户编号查询
	 * @param unid
	 * @return
	 */
	public List<BoCustomerDevice> getByMercNo(String mercNo);
	
	public List<BoCustomerDevice> getByodyNo(String bodyNo);
	
	public int updateIsFirst(String mercNo, String bodyNo);
	
	public List<BoCustomerDevice> getByodyNos(String string);
	
	public int deleteBybodyNOs(String string);
}
