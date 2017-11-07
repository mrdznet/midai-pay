package com.midai.pay.user.mapper;

import org.apache.ibatis.annotations.Select;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.user.entity.Bank;

public interface BankMapper extends MyMapper<Bank> {
	
	@Select("select shortcode, bankcode,bankname from tbl_bank where bankcode=#{bankcode}")
	public Bank findById(String bankcode);

}
