package com.midai.pay.handpay.provider;

import org.apache.commons.lang3.StringUtils;

import com.midai.pay.handpay.query.HyTztxQuery;

public class HyTztxProvider {

	public String columns = " id, host_trams_ssn hostTramsSsn, req_data reqData, resp_data respData, create_time createTime ";

    public String selectCountByQuery(HyTztxQuery query){
        StringBuffer sql = new StringBuffer(" SELECT COUNT(1) FROM tbl_bo_hy_tztx_log WHERE 1=1 ");
        if(null != query){

        }

        return sql.toString();
    }

    public String selectListByQuery(HyTztxQuery query){
        StringBuffer sql = new StringBuffer(" SELECT " + columns + "  FROM tbl_bo_hy_tztx_log WHERE 1=1 ");

        if(null != query){

        	if(StringUtils.isNotEmpty(query.getHostTransSsn())) {
        		sql.append(" and host_trams_ssn like '%" + query.getHostTransSsn() + "%'");
        	}
        	
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
  
}
