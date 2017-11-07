package com.midai.pay.device.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.device.entity.BoAgentDevice;

public interface BoAgentDeviceMapper extends MyMapper<BoAgentDevice> {

    @InsertProvider(type=com.midai.pay.device.provider.BoAgentDeviceProvider.class,method="batchInsert")
    long batchInsert(Map<String,List<BoAgentDevice>> batchData);

    @DeleteProvider(type=com.midai.pay.device.provider.BoAgentDeviceProvider.class,method="delByBodyNo")
    long delByBodyNo(String bodyNos);

    @UpdateProvider(type=com.midai.pay.device.provider.BoAgentDeviceProvider.class,method="updateInventoryStateByBodyNos")
    long updateInventoryStateByBodyNos(@Param("bodyNos")String bodyNos,@Param("agentNo")String agentNo);

    @Update(" update tbl_bo_agent_device set state=#{state} where device_no=#{deviceid}")
    long updateAgentDeviceStateByDeviceId(@Param("deviceid") String deviceid, @Param("state") Integer state);
    
    @DeleteProvider(type=com.midai.pay.device.provider.BoAgentDeviceProvider.class,method="delByBodyNosAndAgentNo")
    long  delByBodyNosAndAgentNo(@Param("bodyNos")String bodyNos,@Param("agentNo")String agentNo);
}
