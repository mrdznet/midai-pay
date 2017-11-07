package com.midai.pay.device.provider;

import com.midai.pay.device.entity.BoDeviceOperateRecord;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public class BoDeviceOperateRecordProvider {

    public String batchInsert(Map<String,List<BoDeviceOperateRecord>> batchData){
        List<BoDeviceOperateRecord> list = batchData.get("list");
        StringBuffer sql = new StringBuffer("insert into tbl_bo_device_operate_record(batch_no,device_no,create_time) values ");
        MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].batchNo},#'{'list[{0}].deviceNo},#'{'list[{0}].createTime})");
        for (int i = 0; i < list.size(); i++) {
            sql.append(messageFormat.format(new Integer[]{i}));
            sql.append(",");
        }
        sql.setLength(sql.length() - 1);
        return sql.toString();
    }
}
