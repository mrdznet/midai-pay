package com.midai.pay.device.provider;

import org.apache.commons.lang3.StringUtils;

import com.midai.pay.device.query.BoDeviceInOutStorageQuery;

public class BoDeviceInOutStorageProvider {

	
   /*public String columns=" a.device_no deviceNo, '' agentName, b.agent_name  destagentName,b.create_time  createTime,b.app_user appUser, ''  operateState ";
   public String columns1=" d.device_no deviceNo, c.agent_name agentName , c.destagent_name  destagentName,c.create_time createTime, c.app_user appUser,c.operate_state operateState";*/
	
	public String columns=" a.device_no deviceNo, b.agent_name agentName, ''  destagentName,b.create_time  createTime,b.app_user appUser,'0'  operateState ";
	public String columns1=" d.device_no deviceNo, c.agent_name agentName , c.destagent_name  destagentName,c.create_time createTime, c.app_user appUser,c.operate_state operateState ";
	
	
	/**
	 * 出入库明细查询
	 * @param query
	 * @return
	 */
	public String findQueryBoDevice(BoDeviceInOutStorageQuery query){
		
        StringBuffer sql=new StringBuffer("SELECT a.agent_name  agentName,a.destagent_name destagentName,a.create_time createTime,a.state operateState,b.username appUser,a.device_no deviceNo "
        		                       + "from tbl_bo_iostorage a INNER JOIN tbl_system_user b ON a.operator_id=b.id where 1=1");
		sql.append(this.comnStringBuffer(query));
		sql.append(" order by a.create_time DESC limit "+ (query.getPageNumber()-1)*query.getPageSize()+","+query.getPageSize());
		return sql.toString();
	}
	
	public String findQueryBoDeviceCount(BoDeviceInOutStorageQuery query){
		
		 StringBuffer sql=new StringBuffer("SELECT count(1) "
                 + "from tbl_bo_iostorage a INNER JOIN tbl_system_user b ON a.operator_id=b.id where 1=1");
		
		 sql.append(this.comnStringBuffer(query));
		 return sql.toString();
	}
	
	private String comnStringBuffer(BoDeviceInOutStorageQuery query){
		StringBuffer sql=new StringBuffer("");
		if(query!=null){
			if(StringUtils.isNotBlank(query.getDeviceNoBegin())&&StringUtils.isNotBlank(query.getDeviceNoEnd())){
				sql.append(" and a.device_no in("+query.getDeviceNobn()+") ");
			}else if(StringUtils.isNotBlank(query.getDeviceNoBegin())){
				sql.append(" and a.device_no like '%" + query.getDeviceNoBegin() + "%' ");  //like '%" + query.getDeviceNoBegin() + "%'
			}
			else if(StringUtils.isNotBlank(query.getDeviceNoEnd())){
				sql.append(" and a.device_no like '%" + query.getDeviceNoEnd() + "%' ");    //like '%" + query.getDeviceNoEnd() + "%'
			}
			if(!StringUtils.isEmpty(query.getCreateTimeBegin())){          
				sql.append(" and date_format(a.create_time,'%Y-%m-%d') >= #{createTimeBegin}");
			}
			if(!StringUtils.isEmpty(query.getCreateTimeEnd())){
				sql.append(" and date_format(a.create_time,'%Y-%m-%d') <= #{createTimeEnd}");
			}
            if(!StringUtils.isEmpty(query.getOperateState())){
				sql.append("and a.state = #{operateState}");
			}
		}
		return sql.toString();
		
	}
	
	//支持excel下载
	public String ExcelDownBoDevice(BoDeviceInOutStorageQuery query){
	
		StringBuffer sql=new StringBuffer("SELECT a.agent_name  agentName,a.destagent_name destagentName,a.create_time createTime,a.state operateState,b.username appUser,a.device_no deviceNo "
                + "from tbl_bo_iostorage a INNER JOIN tbl_system_user b ON a.operator_id=b.id where 1=1");
	sql.append(this.comnStringBuffer(query));
	
	return sql.toString();
	} 
	
	public String ExcelDownloadBoDeviceCount(BoDeviceInOutStorageQuery query){
		StringBuffer sql=new StringBuffer("SELECT count(1) "
                + "from tbl_bo_iostorage a INNER JOIN tbl_system_user b ON a.operator_id=b.id where 1=1");
				 sql.append(this.comnStringBuffer(query));
				 return sql.toString();
	} 
	
	//支持excel下载有复选框//暂时是不用的
	public String ExcelDownSelectBoDevice(String deviceNos){
		
		StringBuffer sql=new StringBuffer(" select p.deviceNo, p.agentName, p.destagentName, p.createTime, p.appUser,p.operateState from ( select  a.device_no deviceNo, b.agent_name agentName,  ''  destagentName,b.create_time  createTime,b.app_user appUser, case a.state  WHEN '1' THEN '入库' WHEN '2' THEN '入库'WHEN '3' THEN '入库' WHEN '4' THEN '入库' END operateState  from tbl_bo_device_instorage b "
		+ " LEFT JOIN tbl_bo_device a ON a.rk_no=b.rk_no "
		+ " UNION ALL select d.device_no deviceNo, c.agent_name agentName , c.destagent_name  destagentName,c.create_time createTime, c.app_user appUser,case c.operate_state  WHEN '1' THEN '出库'when '2' THEN '变更'END operateState from tbl_bo_device_outstorage c  "
		+ " LEFT JOIN tbl_bo_device_operate_record d ON d.batch_no=c.ck_no ) p  where 1=1 ");
		
		
		
		if(StringUtils.isNotBlank(deviceNos)){
			sql.append(" and p.deviceNo in("+deviceNos+")");
		}
		
		return sql.toString();
	}
	
	
}
