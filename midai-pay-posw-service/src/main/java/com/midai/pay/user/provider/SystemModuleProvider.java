package com.midai.pay.user.provider;

import java.util.Map;

import com.midai.pay.common.po.SqlParam;

public class SystemModuleProvider {
	
	public String findAllSource(Map<String, String> param){
		String roles = param.get("roles");
		String pageLevel = param.get("pageLevel");
		String sql = " SELECT rm.module_id moduleId, m.page_id pageId, m.page_parent_id pageParentId, m.page_level pageLevel "
					+" FROM tbl_system_role_module rm, tbl_system_module m"
					+" WHERE rm.module_id=m.module_id AND rm.role_id in ("+roles+") and m.page_level="+pageLevel;
		
		return sql;		
	}
	
	public String findModuleInfo(String moduleIDs){
		String sql = " SELECT m.module_id moduleId, m.page_id pageId, m.page_parent_id pageParentId, m.page_level pageLevel "
					+" FROM tbl_system_module m WHERE m.module_id in ("+moduleIDs+")";
		
		return sql;
	}
	
	public String findAllParentInfo(String childIds){
		String sql = " SELECT t.module_id moduleId, t.page_id pageId, t.page_parent_id pageParentId, t.page_level pageLevel"
					+" FROM tbl_system_module t "
					+" where t.module_id in (SELECT m.page_parent_id FROM tbl_system_module m WHERE m.module_id in("+childIds+")) ";
		
		return sql;
	}
	
	public String findUserByModuleIdAndInscode(SqlParam param){
		String sql = " SELECT DISTINCT u.loginname"
					+" FROM tbl_system_user_role r LEFT JOIN tbl_system_user u ON r.userid=u.id"
					+" WHERE (u.inscode=#{param_1} or u.inscode is null) " //还要发送集团下的所有用户，这些用户的inscode都为null
					+" 	AND r.roleid IN (SELECT m.role_id FROM tbl_system_role_module m WHERE m.module_id=#{param_4} )";
		
		return sql;
	}
}
