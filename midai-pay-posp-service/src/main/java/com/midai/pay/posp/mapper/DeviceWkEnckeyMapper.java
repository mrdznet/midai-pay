package com.midai.pay.posp.mapper;

import org.apache.ibatis.annotations.Update;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.posp.entity.DeviceWkEnckey;

public interface DeviceWkEnckeyMapper extends MyMapper<DeviceWkEnckey> {
	
	
	@Update("update tbl_device_wk_enc_key set term_pik_enc_key = #{termPikEncKey},"
			+ " term_mac_enc_key = #{termMacEncKey},"
			+ " term_tck_enc_key = #{termTckEncKey}"
			+ " where device_no =  #{deviceNo} and merc_id = #{mercId}")
	int updateWkEncKey(DeviceWkEnckey deviceWkEnckey);
	
	
	

}
