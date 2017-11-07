package com.midai.pay.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.agent.entity.Agent;
import com.midai.pay.device.entity.BoDeviceMode;
import com.midai.pay.device.query.BoDeviceModeQuery;
import com.midai.pay.device.vo.DeviceModeVo;

public interface BoDeviceModeMapper extends MyMapper<BoDeviceMode> {
	
	@SelectProvider(type=com.midai.pay.device.provider.BoDeviceModeProvider.class, method="queryCount")
	public int queryCount();
	
	@SelectProvider(type=com.midai.pay.device.provider.BoDeviceModeProvider.class, method="queryList")
	public List<DeviceModeVo> queryList(BoDeviceModeQuery query);
	
	@UpdateProvider(type=com.midai.pay.device.provider.BoDeviceModeProvider.class, method="updateState")
	public Integer updateState(@Param("ids") String ids, @Param("state") Integer state, @Param("userName") String userName);

	@Select("SELECT A.*  FROM tbl_bo_device_mode A INNER JOIN tbl_bo_device B ON A.id=B.devicemode_id  WHERE B.device_no=#{deviceid}")
	public Agent getByDeviceNo(String deviceid);


}
