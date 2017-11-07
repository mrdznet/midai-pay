package com.midai.pay.device.mapper;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.device.entity.BoDeviceOperateRecord;
import org.apache.ibatis.annotations.InsertProvider;

import java.util.List;
import java.util.Map;

public interface BoDeviceOperateRecordMapper extends MyMapper<BoDeviceOperateRecord> {

    @InsertProvider(type=com.midai.pay.device.provider.BoDeviceOperateRecordProvider.class,method="batchInsert")
    long batchInsert(Map<String,List<BoDeviceOperateRecord>> batchData);
}
