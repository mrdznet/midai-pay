package com.midai.pay.user.provider;

import java.util.Map;

public class MobileversionProvider {
	
	public String columns = " version_id, andriod_version_id, contents, ios_load_url, android_load_url, ios_flag, android_flag, active_ind, create_time, update_time ";
	
	public String getLastVersion(Map<String, String> param){
		StringBuffer  sql = new StringBuffer();
		String device = param.get("device");
		String type = param.get("type");
		sql.append("SELECT ");
		if("Android".equals(device)){
			sql.append("andriod_version_id versionId, ");
		}else if("IOS".equals(device)){
			sql.append("version_id versionId, ");
		}
		sql.append(" contents, ios_load_url iosLoadUrl, android_load_url androidLoadUrl,  case when ISNULL(ios_flag) then 0 else 1 end iosFlag,  "
				+ " case when ISNULL(android_flag) then 0 else 1 end androidFlag, version_size versionSize " +
				" FROM tbl_mobile_app_version where active_ind=1 and type=" + type + " ORDER BY create_time DESC  limit 0,1"); 
		
		return sql.toString();
	}
	
	
}
