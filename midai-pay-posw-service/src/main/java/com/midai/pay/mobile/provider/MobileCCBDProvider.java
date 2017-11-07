package com.midai.pay.mobile.provider;

import java.util.Map;

public class MobileCCBDProvider {
	
    public String getDeviceModeTypeFactoryVoByPostalCode(Map<String, String> map){
    	String postalCode = map.get("postalCode");
    	String code = map.get("code");
        StringBuffer sql=new StringBuffer(" SELECT mo.name AS deviceModeName, mo.image_path imagePath, ty.name AS deviceTypeName FROM tbl_bo_device_mode mo  INNER JOIN  tbl_bo_device_type ty ON mo.device_type_id=ty.id  ");
        sql.append("  left join tbl_bo_factory f on mo.factory_id=f.id  ");
        sql.append("  where mo.state=1  ");
        sql.append("  and ty.state=1  ");
        if(code.equals("399143")) {
        	sql.append("    and mo.name like '%海贝%'  ");
        } else {
        	sql.append("   and f.postal_code = '" + postalCode + "'  ");
        }
        
        return sql.toString();
    }

}
