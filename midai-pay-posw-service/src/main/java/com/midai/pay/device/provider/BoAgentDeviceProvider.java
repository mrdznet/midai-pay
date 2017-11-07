package com.midai.pay.device.provider;

import com.midai.pay.device.entity.BoAgentDevice;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public class BoAgentDeviceProvider {

    public String batchInsert(Map<String,List<BoAgentDevice>> batchData){
        List<BoAgentDevice> list = batchData.get("list");
        StringBuffer sql = new StringBuffer("insert into tbl_bo_agent_device(agent_id,device_no,create_time) values ");
        MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].agentId},#'{'list[{0}].deviceNo},#'{'list[{0}].createTime})");
        for (int i = 0; i < list.size(); i++) {
            sql.append(messageFormat.format(new Integer[]{i}));
            sql.append(",");
        }
        sql.setLength(sql.length() - 1);
        return sql.toString();
    }

    public String delByBodyNo(String bodyNos){
        StringBuffer sql = new StringBuffer("delete from tbl_bo_agent_device where device_no in(");
        sql.append(bodyNos+")");
        return sql.toString();
    }

    public String updateInventoryStateByBodyNos(Map<String,Object> param){
        StringBuffer sql = new StringBuffer("update tbl_bo_agent_device set is_inventory=2 where agent_id=#{agentNo} and device_no in(");
        sql.append(param.get("bodyNos")+")");
        return sql.toString();
    }
    
    public String delByBodyNosAndAgentNo(Map<String,Object> param){
        StringBuffer sql = new StringBuffer("delete from tbl_bo_agent_device where agent_id=#{agentNo} and device_no in(");
        sql.append(param.get("bodyNos")+")");
        return sql.toString();
    }
    
    
}
