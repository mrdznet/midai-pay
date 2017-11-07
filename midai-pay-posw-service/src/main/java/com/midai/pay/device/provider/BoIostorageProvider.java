package com.midai.pay.device.provider;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import com.midai.pay.device.entity.BoIostorage;

public class BoIostorageProvider {

    public String batchInsertBIo(Map<String,List<BoIostorage>> batchData){
        List<BoIostorage> list = batchData.get("list");
        StringBuffer sql = new StringBuffer("insert into tbl_bo_iostorage(device_no, agent_id, agent_name, destagent_id, destagent_name, state, operator_id, create_time) values ");
        MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].deviceNo},#'{'list[{0}].agentId},#'{'list[{0}].agentName},#'{'list[{0}].destagentId},#'{'list[{0}].destagentName},#'{'list[{0}].state}, #'{'list[{0}].operatorId},#'{'list[{0}].createTime})");
        for (int i = 0; i < list.size(); i++) {
            sql.append(messageFormat.format(new Integer[]{i}));
            sql.append(",");
        }
        sql.setLength(sql.length() - 1);
        return sql.toString();
    }

}
