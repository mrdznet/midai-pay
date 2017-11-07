package com.midai.pay.customer.mapper;

import java.util.List;

import org.apache.ibatis.annotations.SelectProvider;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.customer.entity.BoCustomerLevel;
import com.midai.pay.customer.query.BoCustomerLevelModeQuery;
import com.midai.pay.customer.vo.BoCustomerLevelVo;

public interface BoCustomerLevelMapper extends MyMapper<BoCustomerLevel> {
	
	@SelectProvider(type=com.midai.pay.customer.provider.BoCustomerLevelProvider.class, method="queryCount")
	public int queryCount();
	
	@SelectProvider(type=com.midai.pay.customer.provider.BoCustomerLevelProvider.class, method="queryList")
	public List<BoCustomerLevelVo> queryList(BoCustomerLevelModeQuery query);
}
