package com.midai.pay.user.provider;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import com.midai.pay.user.entity.SystemUserRole;

public class SystemUserRoleProvider {

	public String insertBatch(Map<String, List<SystemUserRole>> map) {
		
		List<SystemUserRole> list = map.get("list");
		StringBuffer sql = new StringBuffer("insert into tbl_system_user_role(roleid,userid,create_time) values ");
		MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].roleid},#'{'list[{0}].userid},#'{'list[{0}].createTime})");
		for (int i = 0; i < list.size(); i++) {
			sql.append(messageFormat.format(new Integer[]{i}));
			sql.append(",");
		}
		sql.setLength(sql.length() - 1);
		return sql.toString();
	}
	
	public String findUserByRoleAndInscode(Map<String, String> paraMap){
		String sql = " SELECT DISTINCT u.loginname FROM tbl_system_user_role r LEFT JOIN tbl_system_user u ON r.userid=u.id"
					+" WHERE u.inscode=" + paraMap.get("inscode") + "  AND r.roleid="+paraMap.get("roleId");
		
		return sql;
	}
}
