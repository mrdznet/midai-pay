package com.midai.pay.device.provider;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.midai.pay.common.constants.Constants;
import com.midai.pay.device.query.BoDeviceTypeQuery;

public class BoDeviceTypeProvider {
	
	public String columns = "id,name,state,note,create_time createTime,create_user createUser,update_time updateTime,update_user updateUser";

	public String queryCount() {
		StringBuffer sql = new StringBuffer("select count(1) from tbl_bo_device_type where state <> " + Constants.DEVICE_TYPE_STATE_ABOLISH);
		return sql.toString();
	}
	
	public String queryList(BoDeviceTypeQuery query) {
		StringBuffer sql = new StringBuffer("select " + columns+ " from tbl_bo_device_type where state <> " + Constants.DEVICE_TYPE_STATE_ABOLISH);
		if (query != null) {
/*			if (StringUtils.isNotBlank(query.getOrder())) {
				sql.append(" order by create_time " + query.getOrder());
			} else {*/
				sql.append(" order by create_time desc ");
//			}
			if (query.getPageSize() > 0) {
				sql.append(" limit " + (query.getPageNumber()-1)*query.getPageSize()+","+query.getPageSize());
			}
		}
		return sql.toString();
	}
	
	public String updateState(Map<String, Object> map) {
		if(map == null || map.isEmpty()) {
			throw new RuntimeException("{\"errorMsg\":\"删除设备类型参数异常！\"}");
		}
		Integer state = Integer.parseInt(map.get("state").toString());
		String userName = String.valueOf(map.get("userName"));
		String ids = String.valueOf(map.get("ids"));
		if(state == null || StringUtils.isBlank(userName) || StringUtils.isBlank(ids)) {
			throw new RuntimeException("{\"errorMsg\":\"删除设备类型参数异常！\"}");
		}
		StringBuffer sql = new StringBuffer("update tbl_bo_device_type set state = ");
		sql.append(state).append(", update_user = '").append(userName);
		sql.append("' where id in(").append(ids).append(")");
		return sql.toString();
	}

}
