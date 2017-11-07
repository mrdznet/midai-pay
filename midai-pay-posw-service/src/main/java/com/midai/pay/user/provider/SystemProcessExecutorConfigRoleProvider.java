package com.midai.pay.user.provider;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import com.midai.pay.user.entity.SystemProcessExecutorConfigRole;

public class SystemProcessExecutorConfigRoleProvider {

	public String insertBatch(Map<String, List<SystemProcessExecutorConfigRole>> map) {
		
		List<SystemProcessExecutorConfigRole> list = map.get("list");
		StringBuffer sql = new StringBuffer("insert into tbl_process_executor_config_role(roleid,processid,create_time) values ");
		MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].roleid},#'{'list[{0}].processid},#'{'list[{0}].createTime})");
		for (int i = 0; i < list.size(); i++) {
			sql.append(messageFormat.format(new Integer[]{i}));
			sql.append(",");
		}
		sql.setLength(sql.length() - 1);
		return sql.toString();
	}
}
