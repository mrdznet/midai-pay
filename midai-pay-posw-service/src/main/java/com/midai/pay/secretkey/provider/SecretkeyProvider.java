package com.midai.pay.secretkey.provider;


public class SecretkeyProvider {

	public String findCount(String begin)
	{
		StringBuffer sql=new StringBuffer(" select count(1) CC from  tbl_secretkey where device_no like'%"+begin+"%'");
	    return sql.toString();
	}
	
	public String findMaxDeviceNo(String begin)
	{
		StringBuffer sql=new StringBuffer(" select max(device_no) CC from  tbl_secretkey where device_no like'%"+begin+"%'");
	    return sql.toString();
	}
	
}
