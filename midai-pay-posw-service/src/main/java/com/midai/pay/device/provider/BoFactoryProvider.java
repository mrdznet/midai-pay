package com.midai.pay.device.provider;

import com.midai.pay.device.query.BoFactoryModeQuery;
import org.apache.commons.lang3.StringUtils;
import com.midai.pay.device.query.BoFactoryQuery;

public class BoFactoryProvider {

    public String paginageAllDeviceMode(BoFactoryModeQuery query){
        StringBuffer sql=new StringBuffer("select bf.id AS factoryId,bf.name AS factoryName,bdm.id AS modeId,bdm.name AS modeName,bdt.id AS typeId,bdt.name AS typeName");
        sql.append(" from tbl_bo_device_mode bdm INNER JOIN tbl_bo_factory bf ON bdm.factory_id=bf.id ");
        sql.append(" INNER JOIN tbl_bo_device_type bdt ON bdt.id=bdm.device_type_id where bf.state=1 and bdm.state=1 and bdt.state=1");
        if(query!=null){
            if(StringUtils.isNotBlank(query.getFactoryName())){
                sql.append(" and locate(#{factoryName},bf.name)");
            }
            if(StringUtils.isNotBlank(query.getModeName())){
                sql.append(" and locate(#{modeName},bdm.name)");
            }
        }
        sql.append(" order by bdm.create_time DESC limit "+ (query.getPageNumber()-1)*query.getPageSize()+","+query.getPageSize());
        return sql.toString();
    }

    public String paginageAllDeviceModeCount(BoFactoryModeQuery query){
        StringBuffer sql=new StringBuffer("select COUNT(1) from tbl_bo_device_mode bdm INNER JOIN tbl_bo_factory bf ON bdm.factory_id=bf.id");
        sql.append(" INNER JOIN tbl_bo_device_type bdt ON bdt.id=bdm.device_type_id where bf.state=1 and bdm.state=1 and bdt.state=1");
        if(query!=null){
            if(StringUtils.isNotBlank(query.getFactoryName())){
                sql.append(" and locate(#{factoryName},bf.name)");
            }
            if(StringUtils.isNotBlank(query.getModeName())){
                sql.append(" and locate(#{modeName},bdm.name)");
            }
        }
        return sql.toString();
    }

    public String factoryCount(BoFactoryQuery query){
        StringBuffer sql = new StringBuffer(" SELECT COUNT(1) FROM tbl_bo_factory WHERE 1=1 ");
        if(null != query){

        }

        return sql.toString();
    }

    public String factoryQuery(BoFactoryQuery query){
        StringBuffer sql = new StringBuffer(" SELECT id, name, address, note, CASE WHEN state=1 THEN '启用' ELSE '暂停' END state  FROM tbl_bo_factory WHERE 1=1 ");

        if(null != query){

        	if (StringUtils.isEmpty(query.getOrder())) {
    			sql.append(" order by create_time " + query.getOrder());
    		} else {
    			sql.append(" order by create_time desc ");
    		}
    		if (query.getPageSize() > 0) {
    			sql.append(" limit " + (query.getPageNumber()-1)*query.getPageSize()+","+query.getPageSize());
    		}
        }
        
		

        return sql.toString();
    }
    
    public String queryFactoryInfo() {
    	StringBuffer sql = new StringBuffer(" SELECT id, name FROM tbl_bo_factory WHERE state = 1 ");
        return sql.toString();
    }
}
