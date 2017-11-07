package com.midai.pay.user.provider;

import org.springframework.util.StringUtils;

import com.midai.pay.user.query.SystemCommNodeQuery;

public class SystemRoleProvider {

	public String columns = "id,rolename,status,description,system,create_time,update_time";

	public String queryRoleList(SystemCommNodeQuery query) {
		StringBuffer sql = new StringBuffer(" SELECT tor.roleid id, tor.orgid pid ,r.rolename name from tbl_system_role r INNER JOIN tbl_system_organization_role tor ");
		sql.append(" on r.id=tor.roleid ");
		sql.append(" where  tor.orgid in (" + query.getPid() + " ) ");
		

		if (!StringUtils.isEmpty(query.getOrder())) {
			sql.append(" order by " + query.getOrder());
		}
		if (query.getPageSize() > 0) {
			sql.append(" limit  "+ (query.getPageNumber()-1)*query.getPageSize()+","+query.getPageSize());
		}

		return sql.toString();
	}

	public String queryRoleCount(SystemCommNodeQuery query) {
		StringBuffer sql = new StringBuffer("select count(1) from tbl_system_role r INNER JOIN tbl_system_organization_role tor ");
		sql.append(" on r.id=tor.roleid ");
		sql.append(" where  tor.orgid in (" +  query.getPid() + " ) ");
		
		return sql.toString();
	}

	public String findByorgIds(String orgIds){
		StringBuffer sql = new StringBuffer("select " + columns + " from tbl_system_role r where exists (select 1 from ( select org.roleid from tbl_system_organization_role org where org.orgid in (" + orgIds + ")) t where r.id=t.roleid)");
		return sql.toString();
	}
	
	/*批量删除*/
	public String deleteSystemRole(String ids){
		String sql = "delete from tbl_system_role  where  id  in ( "+ids+")";
		return sql;
	}
	
	public String deleteByRoleIds(String ids){
		String sql = "delete from tbl_system_organization_role  where  roleid  in ( "+ids+")";
		return sql;
	}
	public String deleteByRoleids(String ids){
		String sql = "delete from tbl_system_role_module  where  role_id  in ( "+ids+")";
		return sql;
	}
	
	public String deleteByRoleIdCs(String ids){
		String sql = "delete from  tbl_process_executor_config_role  where  roleid  in ( "+ids+")";
		return sql;
	}
	
	
}
