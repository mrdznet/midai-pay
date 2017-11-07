package com.midai.pay.device.provider;

import com.midai.pay.device.entity.BoDevice;
import com.midai.pay.device.query.BoAgentDeviceQuery;
import com.midai.pay.device.query.BoDeviceExcelQuery;
import com.midai.pay.device.query.BoDeviceQuery;

import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public class BoDeviceProvider {

    public String batchInsert(Map<String,List<BoDevice>> batchData){
        List<BoDevice> list = batchData.get("list");
        StringBuffer sql = new StringBuffer("insert into tbl_bo_device(devicemode_id,rk_no,device_no,sim_no,state,customer_id,device_status,create_time) values ");
        MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].deviceModeId},#'{'list[{0}].rkNo},#'{'list[{0}].deviceNo},#'{'list[{0}].simNo},#'{'list[{0}].state},#'{'list[{0}].customerId},#'{'list[{0}].deviceStatus},#'{'list[{0}].createTime})");
        for (int i = 0; i < list.size(); i++) {
            sql.append(messageFormat.format(new Integer[]{i}));
            sql.append(",");
        }
        sql.setLength(sql.length() - 1);
        return sql.toString();
    }

   public String bacthUpdateCustomerIdAndState(String deviceNos) {
	   StringBuffer sql=new StringBuffer("update tbl_bo_device set customer_id='' where  device_no in ( ");
       sql.append(deviceNos+" )");
       return sql.toString();
   }
    
    public String bacthUpdateIsFirst(String deviceNos)
    {
    	StringBuffer sql=new StringBuffer("update tbl_bo_device set is_first=0 where  device_no in ( ");
        sql.append(deviceNos+" )");
        return sql.toString();
    }
    
    public String fetchByDeviceNos(String deviceNos){
        StringBuffer sql=new StringBuffer("select device_no from tbl_bo_device where device_no in (");
        sql.append(deviceNos+")");
        return sql.toString();
    }

    public String fetchBySimNos(String simNos){
        StringBuffer sql=new StringBuffer("select sim_no from tbl_bo_device where sim_no in (");
        sql.append(simNos+")");
        return sql.toString();
    }

    public String updateByBodyNos(Map<String,Object> param){
        StringBuffer sql=new StringBuffer("update tbl_bo_device set state=#{state} where device_no in (");
        sql.append(param.get("bodyNos")+")");
        return sql.toString();
    }
    //终端出库:追加记录
    public String paginateAgentInventoryDevice(BoAgentDeviceQuery query){
        StringBuffer sql=new StringBuffer("select bd.device_no AS deviceNo,bd.customer_id AS customerId, dt.name AS typeName, dm.name AS modeName "
        		+ "  from tbl_bo_device bd");
        sql.append(" LEFT JOIN tbl_bo_agent_device bad ON bd.device_no=bad.device_no  ");
        sql.append(" LEFT JOIN tbl_bo_device_mode dm ON bd.devicemode_id=dm.id ");
        sql.append(" LEFT JOIN tbl_bo_device_type  dt ON dm.device_type_id=dt.id where bd.device_status=1 ");
        
        if(query!=null){
        	if(StringUtils.isNotBlank(query.getAgentNo())){
        		sql.append(" and bad.agent_id=#{agentNo} ");
        	}
        	
            if(StringUtils.isNotBlank(query.getBodyNoStart())&&(query.getNum()!=null&&query.getNum()>0)){
                if(StringUtils.isNotBlank(query.getBodyNos())){
                    sql.append(" and bd.device_no in ("+query.getBodyNos()+") ");
                }
            }else if(StringUtils.isNotBlank(query.getBodyNoStart())){
                sql.append(" and bd.device_no=#{bodyNoStart}");
            }
        }
        //状态是不可逆的，所以需要拿那个商户号做为判定条件，就是没有商户绑定的时候，该设备可以用
        sql.append("  and  bd.state in (1,2) and (bd.customer_id IS NULL or bd.customer_id='') ");
        sql.append(" order by bd.create_time DESC limit "+ (query.getPageNumber()-1)*query.getPageSize()+","+query.getPageSize());
        return sql.toString();
    }

    public String paginateAgentInventoryDeviceCount(BoAgentDeviceQuery query){
        StringBuffer sql=new StringBuffer("select COUNT(1) from tbl_bo_device bd");
        
        sql.append(" LEFT JOIN tbl_bo_agent_device bad ON bd.device_no=bad.device_no  ");
        sql.append(" LEFT JOIN tbl_bo_device_mode dm ON bd.devicemode_id=dm.id ");
        sql.append(" LEFT JOIN tbl_bo_device_type  dt ON dm.device_type_id=dt.id where bd.device_status=1 ");
        
        if(query!=null){
        	if(StringUtils.isNotBlank(query.getAgentNo())){
        		sql.append(" and bad.agent_id=#{agentNo} ");
        	}
            if(StringUtils.isNotBlank(query.getBodyNoStart())&&(query.getNum()!=null&&query.getNum()>0)){
                if(StringUtils.isNotBlank(query.getBodyNos())){
                    sql.append(" and bd.device_no in ("+query.getBodyNos()+") ");
                }
            }else if(StringUtils.isNotBlank(query.getBodyNoStart())){
                sql.append(" and bd.device_no=#{bodyNoStart}");
            }
        }
        //状态是不可逆的，所以需要拿那个商户号做为判定条件，就是没有商户绑定的时候，该设备可以用
        sql.append("  and  bd.state in (1,2) and (bd.customer_id IS NULL or bd.customer_id='') ");
        return sql.toString();
    }

    public String paginateAgentDevice(BoDeviceQuery query){
        StringBuffer sql=new StringBuffer(" select bd.device_no AS deviceNo,bd.customer_id AS customerId, dt.name AS typeName,dm.name AS modeName,  ");
        sql.append(" (CASE bd.state WHEN 1 THEN '未开通' WHEN 2 THEN '已开通' WHEN 3 THEN '激活' ELSE '状态异常' END) AS stateName,");
        sql.append(" (SELECT count(1) FROM tbl_bo_iostorage bcd WHERE bcd.device_no = bd.device_no AND state = '解绑') AS historyCustCount,bc.mobile,bad.agent_id AS agentId,tba.name AS agentName  from tbl_bo_device bd ");
        
        sql.append(" LEFT JOIN tbl_bo_agent_device bad ON bd.device_no=bad.device_no  ");
        sql.append(" LEFT JOIN tbl_bo_device_mode dm ON bd.devicemode_id=dm.id  ");
        sql.append("  LEFT JOIN tbl_bo_customer  bc  on bd.customer_id=bc.merc_no ");
        sql.append("  LEFT JOIN tbl_bo_agent tba ON tba.agent_no=bad.agent_id ");
        sql.append("  LEFT JOIN tbl_bo_device_type  dt ON dm.device_type_id=dt.id  where bd.device_status=1");
        
        if(query!=null){
           /* if(StringUtils.isNotBlank(query.getDeviceNo())){
                sql.append(" and bd.device_no=#{deviceNo}");
            }*/
        	/*商户手机号查询*/
        	if(StringUtils.isNotBlank(query.getMobile())){
        		sql.append(" and bc.mobile like '%" + query.getMobile() +  "%' ");
        	}
        	if (StringUtils.isNotBlank(query.getAgentNo())) {
				sql.append(" and bad.agent_id in ("+query.getAgentNo()+")");
			}
        	/*设备查询*/
        	
        	if(StringUtils.isNotBlank(query.getDeviceNoStart()) && StringUtils.isNotBlank(query.getDeviceNoEnd())){
        		sql.append(" and bd.device_no in (" + query.getDeviceNobn() + ") ");
        	}
        	else if (StringUtils.isNotBlank(query.getDeviceNoStart())) {
				sql.append(" and bd.device_no like '%" + query.getDeviceNoStart() + "%' ");
			}
        	else if (StringUtils.isNotBlank(query.getDeviceNoEnd())) {
	    		sql.append(" and bd.device_no like '%" + query.getDeviceNoEnd() + "%' ");
			}
            if(StringUtils.isNotBlank(query.getCustomerId())){
            	sql.append(" and bd.customer_id like '%"+query.getCustomerId()+"%'");
            }
            if(StringUtils.isNotBlank(query.getModeName())){
                sql.append(" and bdi.device_mode_name=#{modeName}");
            }
            if(StringUtils.isNotBlank(query.getTypeName())){
                sql.append(" and bdi.device_type_name=#{typeName}");
            }
        }
        sql.append(" order by bad.create_time DESC , bd.device_no  limit "+ (query.getPageNumber()-1)*query.getPageSize()+","+query.getPageSize());
        return sql.toString();
    }

    public String paginateAgentDeviceCount(BoDeviceQuery query){
        StringBuffer sql=new StringBuffer("select COUNT(1) from tbl_bo_device bd ");
        
        sql.append(" LEFT JOIN tbl_bo_agent_device bad ON bd.device_no=bad.device_no  ");
        sql.append(" LEFT JOIN tbl_bo_device_mode dm ON bd.devicemode_id=dm.id  ");
        sql.append("  LEFT JOIN tbl_bo_customer  bc  on bd.customer_id=bc.merc_no ");
        sql.append("  LEFT JOIN tbl_bo_agent tba ON tba.agent_no=bad.agent_id ");
        sql.append("  LEFT JOIN tbl_bo_device_type  dt ON dm.device_type_id=dt.id  where bd.device_status=1");
        
        
        if(query!=null){
        	 /* if(StringUtils.isNotBlank(query.getDeviceNo())){
            sql.append(" and bd.device_no=#{deviceNo}");
            }*/
        	/*商户手机号查询*/
        	if(StringUtils.isNotBlank(query.getMobile())){
        		sql.append(" and bc.mobile like '%" + query.getMobile() +  "%' ");
        	}
        	if (StringUtils.isNotBlank(query.getAgentNo())) {
        		sql.append(" and bad.agent_id in ("+query.getAgentNo()+")");
			}
        	
        	if(StringUtils.isNotBlank(query.getCustomerId())){
            	sql.append(" and bd.customer_id like '%"+query.getCustomerId()+"%'");
            }
        	/*设备查询*/
        	
        	if(StringUtils.isNotBlank(query.getDeviceNoStart()) && StringUtils.isNotBlank(query.getDeviceNoEnd())){
        		sql.append(" and bd.device_no in (" + query.getDeviceNobn() + ") ");
        	}
        	else if (StringUtils.isNotBlank(query.getDeviceNoStart())) {
				sql.append(" and bd.device_no like '%" + query.getDeviceNoStart() + "%' ");
			}
        	else if (StringUtils.isNotBlank(query.getDeviceNoEnd())) {
	    		sql.append(" and bd.device_no like '%" + query.getDeviceNoEnd() + "%' ");
			}
	    	
            if(StringUtils.isNotBlank(query.getModeName())){
                sql.append(" and bdi.device_mode_name=#{modeName}");
            }
            if(StringUtils.isNotBlank(query.getTypeName())){
                sql.append(" and bdi.device_type_name=#{typeName}");
            }
        }
        return sql.toString();
    }
    //终端变更:追加记录
    public String paginateDevice(BoAgentDeviceQuery query){
        StringBuffer sql=new StringBuffer("select bd.device_no AS deviceNo,bd.customer_id AS customerId, dt.name AS typeName, dm.name AS modeName "
        		+ " from tbl_bo_device bd ");
        sql.append(" left JOIN tbl_bo_agent_device bad ON bad.device_no=bd.device_no ");
        sql.append(" LEFT JOIN tbl_bo_device_mode dm ON bd.devicemode_id=dm.id ");
        sql.append(" LEFT JOIN tbl_bo_device_type  dt ON dm.device_type_id=dt.id where bd.device_status=1");
        
        if(query!=null){
            if(StringUtils.isNotBlank(query.getBodyNoStart())&&(query.getNum()!=null&&query.getNum()>0)){
                if(StringUtils.isNotBlank(query.getBodyNos())){
                    sql.append(" and bd.device_no in ("+query.getBodyNos()+") ");
                }
            }else if(StringUtils.isNotBlank(query.getBodyNoStart())){
                sql.append(" and bd.device_no=#{bodyNoStart}");
            }

            if(StringUtils.isNotBlank(query.getAgentNo())){
                sql.append(" and bad.agent_id in (" + query.getAgentNo() + ")");
            }
        }
        sql.append(" order by bd.create_time DESC, bd.device_no  limit "+ (query.getPageNumber()-1)*query.getPageSize()+","+query.getPageSize());
        return sql.toString();
    }

    public String paginateDeviceCount(BoAgentDeviceQuery query){
        StringBuffer sql=new StringBuffer("select COUNT(1) from tbl_bo_device bd");
        sql.append(" left JOIN tbl_bo_agent_device bad ON bad.device_no=bd.device_no ");
        sql.append(" LEFT JOIN tbl_bo_device_mode dm ON bd.devicemode_id=dm.id ");
        sql.append(" LEFT JOIN tbl_bo_device_type  dt ON dm.device_type_id=dt.id where bd.device_status=1");
        
        if(query!=null){
            if(StringUtils.isNotBlank(query.getBodyNoStart())&&(query.getNum()!=null&&query.getNum()>0)){
                if(StringUtils.isNotBlank(query.getBodyNos())){
                    sql.append(" and bd.device_no in ("+query.getBodyNos()+") ");
                }
            }else if(StringUtils.isNotBlank(query.getBodyNoStart())){
                sql.append(" and bd.device_no=#{bodyNoStart}");
            }

            if(StringUtils.isNotBlank(query.getAgentNo())){
            	sql.append(" and bad.agent_id in (" + query.getAgentNo() + ")");
            }
        }
        return sql.toString();
    }

    public String updateDeviceCustomer(Map<String,Object> param){
        StringBuffer sql=new StringBuffer("update tbl_bo_device set customer_id=#{customerId} where device_no in (");
        sql.append(param.get("deviceNos")+")");
        return sql.toString();
    }
    
    public String updateDeviceCustomerAndState(Map<String,Object> param) {
    	StringBuffer sql=new StringBuffer("update tbl_bo_device set customer_id=#{customerId},state=#{state} where device_no in (");
        sql.append(param.get("deviceNos")+")");
        return sql.toString();
	}

    public String excelExportDeviceDetails(BoDeviceQuery query){
        StringBuffer sql=new StringBuffer("select bd.device_no AS bodyNo,bd.sim_no AS simNo,dm.name  AS modeName,dt.name  AS typeName,bc.merc_no AS mercNo,");
        sql.append(" bc.merc_name AS mercName,bc.merc_id AS mercId,bc.agent_id AS agentId,bc.agent_name AS agentName,bc.topagent_id AS topagentId,");
        sql.append(" bc.topagent_name AS topagentName,(CASE bd.customer_id WHEN NULL THEN '未绑定商户' WHEN '' THEN '未绑定商户' ELSE '已绑定商户' END) AS bindState,");
        sql.append(" (CASE bd.device_status WHEN 1 THEN '可用' WHEN 2 THEN '不可用' ELSE '状态异常' END) AS deviceStatus,bc.mobile as mobile  from tbl_bo_device bd");
        sql.append(" LEFT JOIN tbl_bo_device_mode dm ON bd.devicemode_id = dm.id");
        sql.append(" LEFT JOIN tbl_bo_device_type dt ON dm.device_type_id = dt.id");
        sql.append(" LEFT JOIN tbl_bo_agent_device bad on bad.device_no=bd.device_no");
        sql.append(" LEFT JOIN tbl_bo_customer bc ON bc.merc_no=bd.customer_id where  bd.device_status=1");
        if(query!=null){
           /* if(StringUtils.isNotBlank(query.getDeviceNo())){
                sql.append(" and bd.device_no=#{deviceNo}");
            }*/

        	/*商户手机号查询*/
        	if(StringUtils.isNotBlank(query.getMobile())){
        		sql.append(" and bc.mobile like '%" + query.getMobile() +  "%' ");
        	}
        	if (StringUtils.isNotBlank(query.getAgentNo())) {
        		sql.append(" and bad.agent_id in ("+query.getAgentNo()+")");
			}
        	/*设备查询*/
        	
        	if(StringUtils.isNotBlank(query.getDeviceNoStart()) && StringUtils.isNotBlank(query.getDeviceNoEnd())){
        		sql.append(" and bd.device_no in (" + query.getDeviceNobn() + ") ");
        	}
        	else if (StringUtils.isNotBlank(query.getDeviceNoStart())) {
				sql.append(" and bd.device_no like '%" + query.getDeviceNoStart() + "%' ");
			}
        	else if (StringUtils.isNotBlank(query.getDeviceNoEnd())) {
	    		sql.append(" and bd.device_no like '%" + query.getDeviceNoEnd() + "%' ");
			}
            if(StringUtils.isNotBlank(query.getCustomerId())){
                sql.append(" and bc.merc_id=#{customerId}");
            }
            if(StringUtils.isNotBlank(query.getModeName())){
                sql.append(" and bdi.device_mode_name=#{modeName}");
            }
            if(StringUtils.isNotBlank(query.getTypeName())){
                sql.append(" and bdi.device_type_name=#{typeName}");
            }
        }
        return sql.toString();
    }

    public String excelExportDeviceDetailsCount(BoDeviceExcelQuery query){
        StringBuffer sql=new StringBuffer("select COUNT(1) from tbl_bo_device bd");
        sql.append(" LEFT JOIN tbl_bo_device_mode dm ON bd.devicemode_id = dm.id");
        sql.append(" LEFT JOIN tbl_bo_device_type dt ON dm.device_type_id = dt.id");
        sql.append(" LEFT JOIN tbl_bo_agent_device bad on bad.device_no=bd.device_no");
        sql.append(" LEFT JOIN tbl_bo_customer bc ON bc.merc_no=bd.customer_id where  bd.device_status=1");
        if(query!=null){
           /* if(StringUtils.isNotBlank(query.getDeviceNo())){
                sql.append(" and bd.device_no=#{deviceNo}");
            }*/
        	/*商户手机号查询*/
        	if(StringUtils.isNotBlank(query.getMobile())){
        		sql.append(" and bc.mobile like '%" + query.getMobile() +  "%' ");
        	}
        	if (StringUtils.isNotBlank(query.getAgentNo())) {
        		sql.append(" and bad.agent_id in ("+query.getAgentNo()+")");
			}
        	/*设备查询*/
        	
        	if(StringUtils.isNotBlank(query.getDeviceNoStart()) && StringUtils.isNotBlank(query.getDeviceNoEnd())){
        		sql.append(" and bd.device_no in (" + query.getDeviceNobn() + ") ");
        	}
        	else if (StringUtils.isNotBlank(query.getDeviceNoStart())) {
				sql.append(" and bd.device_no like '%" + query.getDeviceNoStart() + "%' ");
			}
        	else if (StringUtils.isNotBlank(query.getDeviceNoEnd())) {
	    		sql.append(" and bd.device_no like '%" + query.getDeviceNoEnd() + "%' ");
			}
            if(StringUtils.isNotBlank(query.getCustomerId())){
                sql.append(" and bc.merc_id=#{customerId}");
            }
            if(StringUtils.isNotBlank(query.getModeName())){
                sql.append(" and bdi.device_mode_name=#{modeName}");
            }
            if(StringUtils.isNotBlank(query.getTypeName())){
                sql.append(" and bdi.device_type_name=#{typeName}");
            }
        }
        return sql.toString();
    }

    public String excelExportSelectedDeviceDetails(String deviceNos){
        StringBuffer sql=new StringBuffer("select bd.device_no AS bodyNo,bd.sim_no AS simNo,bdi.device_mode_name AS modeName,bdi.device_type_name AS typeName,bc.merc_no AS mercNo,");
        sql.append(" bc.merc_name AS mercName,bc.merc_id AS mercId,bc.agent_id AS agentId,bc.agent_name AS agentName,bc.topagent_id AS topagentId,");
        sql.append(" bc.topagent_name AS topagentName,(CASE bd.customer_id WHEN NULL THEN '未绑定商户' WHEN '' THEN '未绑定商户' ELSE '已绑定商户' END) AS bindState,");
        sql.append(" (CASE bd.device_status WHEN 1 THEN '可用' WHEN 2 THEN '不可用' ELSE '状态异常' END) AS deviceStatus from tbl_bo_device bd");
        sql.append(" INNER JOIN tbl_bo_device_instorage bdi ON bdi.rk_no=bd.rk_no");
        sql.append(" LEFT JOIN tbl_bo_customer bc ON bc.merc_no=bd.customer_id where 1=1");
        if(StringUtils.isNotBlank(deviceNos)){
            sql.append(" and bd.device_no in ("+deviceNos+")");
        }
        return sql.toString();
    }

}
