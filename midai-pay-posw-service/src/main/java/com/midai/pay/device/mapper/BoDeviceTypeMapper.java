package com.midai.pay.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.device.entity.BoDeviceType;
import com.midai.pay.device.query.BoDeviceTypeQuery;import com.midai.pay.device.vo.DeviceTypeVo;

public interface BoDeviceTypeMapper extends MyMapper<BoDeviceType> {

	@SelectProvider(type=com.midai.pay.device.provider.BoDeviceTypeProvider.class, method="queryCount")
	public int queryCount();
	
	@SelectProvider(type=com.midai.pay.device.provider.BoDeviceTypeProvider.class, method="queryList")
	public List<BoDeviceType> queryList(BoDeviceTypeQuery query);
	
	@UpdateProvider(type=com.midai.pay.device.provider.BoDeviceTypeProvider.class, method="updateState")
	public Integer updateState(@Param("ids") String ids, @Param("state") Integer state, @Param("userName") String userName);
	
	@Select("select id from tbl_bo_device_type where name = #{name}")
	public List<Integer> getIdByName(String name);
	
	@Select("select id, name from tbl_bo_device_type where state = 1 ")
	public List<DeviceTypeVo> allDeviceType();
}
