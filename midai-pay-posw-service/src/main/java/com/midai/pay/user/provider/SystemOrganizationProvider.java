package com.midai.pay.user.provider;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Select;

import com.midai.pay.user.query.BankBranchQuery;
import com.midai.pay.user.query.SystemCommNodeQuery;

public class SystemOrganizationProvider {
	
	
	public String queryOrgAndUserCount(SystemCommNodeQuery query){
		StringBuffer sb = new StringBuffer(" SELECT COUNT(1) FROM ( "); 
		sb.append(" select o.organization_id id, o.parent_id pid,o.organization_name name, o.org_type org_type, o.update_time from tbl_system_organization o where o.parent_id= #{pid} and o.status=1 ");
		sb.append(" union  ");
		sb.append(" select CONCAT('user_',u.id) id, u.orgid pid, u.username name, 4 org_type, u.update_time from tbl_system_user u where u.orgid=#{pid} and u.status=1 ) t ");
		if(null != query){
			
			if(StringUtils.isNotBlank(query.getName())){
				sb.append(" AND t.name= #{name}");
			}
			
		}
		
		return sb.toString();
	}
	
	public String queryOrgAndUserList(SystemCommNodeQuery query){
		StringBuffer sb = new StringBuffer("select t.id, t.pid, t.name, t.org_type from ( ");
		sb.append(" select o.organization_id id, o.parent_id pid,o.organization_name name, o.org_type org_type, o.update_time from tbl_system_organization o where o.parent_id= #{pid} and o.status=1 ");
		sb.append(" union  ");
		sb.append(" select CONCAT('user_',u.id) id, u.orgid pid, u.username name, 4 org_type, u.update_time from tbl_system_user u where u.orgid=#{pid} and u.status=1  ) t ");
		if(null != query){
			
			if(StringUtils.isNotBlank(query.getName())){
				sb.append(" AND t.name= #{name}");
			}
			
			if (query.getPageSize() > 0) {
				sb.append(" LIMIT  "+ (query.getPageNumber()-1)*query.getPageSize()+","+query.getPageSize());
			}
		}
		
		return sb.toString();
	}
	
	public String getByParentIdAndNameAndStatus(Map<String, String> map) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select organization_id organizationId,parent_id parentId,organization_name organizationName,t_level tLevel,province_code provinceCode,province_name provinceName,city_code cityCode,city_name cityName,org_type orgType,agent_num agentNum ");
		sql.append("  from  tbl_system_organization where parent_id=" + map.get("pid") + " and organization_name like '%" + map.get("name") + "%' ");
		String status = map.get("status");
		if(StringUtils.isNotEmpty(status)) {
			sql.append(" and status= " + status);
		}
		String orgType = map.get("orgType");
		if(StringUtils.isNotEmpty(orgType)) {
			sql.append("  and org_type=" + orgType);
		}
		return sql.toString();
	}
}
