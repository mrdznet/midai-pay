package com.midai.pay.device.provider;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.midai.pay.common.constants.Constants;
import com.midai.pay.device.query.BoDeviceModeQuery;

public class BoDeviceModeProvider {
	
	public String columns = "m.id,m.name,m.factory_id factoryId,f.name factoryName,m.device_type_id deviceTypeId,t.name deviceTypeName,m.state,m.note,m.create_time createTime,m.create_user createUser,m.update_time updateTime,m.update_user updateUser";

	public String queryCount() {
		StringBuffer sql = new StringBuffer("select count(1) from tbl_bo_device_mode m inner join tbl_bo_device_type t on m.device_type_id = t.id ");
		sql.append("inner join tbl_bo_factory f on m.factory_id = f.id where m.state <> " + Constants.DEVICE_MODE_STATE_ABOLISH);
		return sql.toString();
	}
	
	public String queryList(BoDeviceModeQuery query) {
		StringBuffer sql = new StringBuffer("select " + columns+ " from tbl_bo_device_mode m inner join tbl_bo_device_type t on m.device_type_id = t.id ");
		sql.append("inner join tbl_bo_factory f on m.factory_id = f.id where m.state <> " + Constants.DEVICE_MODE_STATE_ABOLISH);
		if (query != null) {
		/*	if (StringUtils.isNotBlank(query.getOrder())) {
				sql.append(" order by m.create_time " + query.getOrder());
			} else {*/
				sql.append(" order by m.create_time desc ");
//			}
			if (query.getPageSize() > 0) {
				sql.append(" limit " + (query.getPageNumber()-1)*query.getPageSize()+","+query.getPageSize());
			}
		}
		return sql.toString();
	}
	
	public String updateState(Map<String, Object> map) {
		if(map == null || map.isEmpty()) {
			throw new RuntimeException("{\"errorMsg\":\"删除设备型号参数异常！\"}");
		}
		Integer state = Integer.parseInt(map.get("state").toString());
		String userName = String.valueOf(map.get("userName"));
		String ids = String.valueOf(map.get("ids"));
		if(state == null || StringUtils.isBlank(userName) || StringUtils.isBlank(ids)) {
			throw new RuntimeException("{\"errorMsg\":\"删除设备型号参数异常！\"}");
		}
		StringBuffer sql = new StringBuffer("update tbl_bo_device_mode set state = ");
		sql.append(state).append(", update_user = '").append(userName);
		sql.append("' where id in(").append(ids).append(")");
		return sql.toString();
	}

}
