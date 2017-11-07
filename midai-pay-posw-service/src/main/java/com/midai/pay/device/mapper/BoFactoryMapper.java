package com.midai.pay.device.mapper;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.device.entity.BoFactory;
import com.midai.pay.device.query.BoFactoryModeQuery;
import com.midai.pay.device.vo.DeviceFactoryModeVo;
import org.apache.ibatis.annotations.SelectProvider;
import com.midai.pay.device.query.BoFactoryQuery;
import com.midai.pay.device.vo.FactoryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import java.util.List;

public interface BoFactoryMapper extends MyMapper<BoFactory> {

    @SelectProvider(type=com.midai.pay.device.provider.BoFactoryProvider.class,method="paginageAllDeviceMode")
    public List<DeviceFactoryModeVo> paginageAllDeviceMode(BoFactoryModeQuery query);

    @SelectProvider(type=com.midai.pay.device.provider.BoFactoryProvider.class,method="paginageAllDeviceModeCount")
    public int paginageAllDeviceModeCount(BoFactoryModeQuery query);

    @SelectProvider(type=com.midai.pay.device.provider.BoFactoryProvider.class, method="factoryCount")
    int factoryCount(BoFactoryQuery query);

    @SelectProvider(type=com.midai.pay.device.provider.BoFactoryProvider.class, method="factoryQuery")
    List<FactoryVo> factoryQuery(BoFactoryQuery query);

    @Update(" UPDATE tbl_bo_factory SET state=0 WHERE id=#{id} ")
    int updateState(@Param("id")int id);
    
    @SelectProvider(type=com.midai.pay.device.provider.BoFactoryProvider.class, method="queryFactoryInfo")
    public List<FactoryVo> queryFactoryInfo();
}
