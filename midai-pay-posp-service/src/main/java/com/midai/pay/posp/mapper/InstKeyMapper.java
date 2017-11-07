package com.midai.pay.posp.mapper;

import org.apache.ibatis.annotations.Update;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.posp.entity.InstKey;

public interface InstKeyMapper extends MyMapper<InstKey> {

	
	@Update("update tbl_inst_key set "
			+ " pin_enc_key = #{pinEncKey},"
			+ " mac_enc_key = #{macEncKey},"
			+ " tck_enc_key = #{tckEncKey}"
			+ " where inst_code = #{instCode}")
	int updateInstKey(InstKey instKey);
	
}
