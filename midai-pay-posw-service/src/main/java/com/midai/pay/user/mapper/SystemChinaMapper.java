package com.midai.pay.user.mapper;

import org.apache.ibatis.annotations.Select;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.user.entity.SystemChina;

public interface SystemChinaMapper extends MyMapper<SystemChina> {
	
	@Select("select code,name,father from tbl_system_china where code=#{code}")
	public SystemChina findByCode(String code);
}
