package com.midai.pay.mobile.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.mobile.entity.CustomerAuthInfoEntity;

public interface CustomerAuthMapper extends MyMapper<CustomerAuthInfoEntity>{
	
	
	@Select("select id from tbl_bo_customer_auth where login_tel = #{mobile} and bank_card = #{bankCard}")
	CustomerAuthInfoEntity getByMobileAndBankCard(@Param("mobile")String mobile,@Param("bankCard")String bankCard);
	
}
