package com.midai.pay.mobile.provider;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class MobileHislogProvider {
	
    public String searchHislog(Map<String, String> map){
    	String phonenumber = map.get("phonenumber");
    	String current = map.get("current");
    	String code = map.get("code");
    	String subSql = "";
    	int difmonth = 1;
    	if(code.equals("399008")) {
    		subSql = " and b.trans_status=0 ";
    		String monthNum = map.get("monthNum");
    		if(StringUtils.isNotEmpty(monthNum)) {
    			difmonth = Integer.parseInt(monthNum);
    		}
    	} else {
    		subSql = " and a.state=4 and b.trans_status=0 ";
    	}
        StringBuffer sql=new StringBuffer(" select b.trans_time transrecvdate, b.mchnt_code_in mchntcodein, a.merc_name mercname, b.host_trans_ssn hosttransssn, b.trans_code_name transcodename,  ");
        sql.append("  b.trans_code TRANSCODE,b.trans_status TRANSST ,b.trans_amt transamt, b.trans_card_no PRIACCTNO, b.resp_cd_loc RESPCDLOC, b.resp_cd_loc_dsp   ");
        sql.append("   from  tbl_deal_total b left join tbl_bo_customer a on b.mobile=a.mobile  ");
        sql.append("  where b.mobile='" + phonenumber + "'" + subSql + " and b.mchnt_code_in=a.merc_id and b.trans_code='0200' and DATE_FORMAT(b.create_time,'%Y%m%d')   ");
        sql.append("  BETWEEN date_add(DATE_FORMAT('" + current + "','%Y%m%d'), interval -" + difmonth + " month) and DATE_FORMAT('" + current + "','%Y%m%d') order by b.create_time desc  ");
        
        if(code.equals("399008")) {
        	Integer lineNumber = Integer.parseInt(map.get("lineNumber"));
        	int start = (Integer.parseInt(map.get("pageNow"))-1)*lineNumber;
        	sql.append(" limit " + start + ", " + lineNumber + " ");
        }
        return sql.toString();
    }

}
