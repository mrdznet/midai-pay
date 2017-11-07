package com.midai.pay.customer.service;

import java.util.List;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseService;
import com.midai.pay.common.po.ReturnVal;
import com.midai.pay.customer.entity.BoCustomerLevel;
import com.midai.pay.customer.query.BoCustomerLevelModeQuery;
import com.midai.pay.customer.vo.BoCustomerLevelVo;
import com.midai.pay.customer.vo.CustomerLevelSimpleVo;

@Service
public interface BoCustomerLevelService extends BaseService<BoCustomerLevel> {
	int queryCount();
	List<BoCustomerLevelVo> queryList(BoCustomerLevelModeQuery query);
	
	ReturnVal<String> insert(BoCustomerLevelVo boCustomerLevelVo);
	
	BoCustomerLevelVo load(String level);
	
	int update(BoCustomerLevelVo boCustomerLevelVo);
	
	int delete(String level);
	
	List<CustomerLevelSimpleVo> getAll();
}
