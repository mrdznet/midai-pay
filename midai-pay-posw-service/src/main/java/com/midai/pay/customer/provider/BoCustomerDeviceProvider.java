package com.midai.pay.customer.provider;

import java.util.List;
import java.util.Map;

import com.midai.pay.common.pay.DateUtils;
import com.midai.pay.customer.entity.BoCustomerDevice;

public class BoCustomerDeviceProvider {

	public String column = "id Id, merc_id mercId, device_id deviceId, merc_no mercNo,body_no bodyNo,state,bunding_time bundingTime, unbunding_time unbundingTime, create_time createTime,is_first isFirst";
	
	public String updateStateAndTime(Map<String,Object> param)
	{
		StringBuffer sql=new StringBuffer("update tbl_bo_customer_device set state=0, unbunding_time=now() WHERE merc_no=#{mercNo} AND body_no in( ");
		sql.append(param.get("bodyNos")+")");
		return sql.toString();
		
	}
	
	public String getByodyNos(String bodyNos) {
		StringBuffer sql=new StringBuffer(" select " + column + " from tbl_bo_customer_device where body_no in (");
		sql.append(bodyNos+")");
		return sql.toString();
	}
	
	public String deleteBybodyNOs(String bodyNos) {
	    
	    StringBuffer sql=new StringBuffer(" delete from  tbl_bo_customer_device where body_no in (");
		sql.append(bodyNos+")");
		return sql.toString();
	}
	
	public String batchInsertCustomerDevices(Map<String,List<BoCustomerDevice>> param ) {
		StringBuffer sb = new StringBuffer("insert into tbl_bo_customer_device (merc_id,merc_no,body_no,state,bunding_time,create_time,is_first) values ");
		for (BoCustomerDevice bd : param.get("list")) {
			sb.append("('"+bd.getMercId()+"','"+bd.getMercNo()+"','"+bd.getBodyNo()+"',"+bd.getState()+",DATE_FORMAT('"+DateUtils.format(bd.getBundingTime(), "yyyy-MM-dd HH:mm:ss")+"','%Y-%m-%d %H:%i:%s'),DATE_FORMAT('"+DateUtils.format(bd.getCreateTime(), "yyyy-MM-dd HH:mm:ss")+"','%Y-%m-%d %H:%i:%s'),"+bd.getIsFirst()+"),");
		}
		sb = sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
}
