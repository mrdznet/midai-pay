package com.midai.pay.user.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.SelectProvider;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.user.entity.Mobileversion;

public interface MobileversionMapper extends MyMapper<Mobileversion> {

	@SelectProvider(type=com.midai.pay.user.provider.MobileversionProvider.class, method="getLastVersion")
	Mobileversion getLastVersion(Map<String, String> param);

}
