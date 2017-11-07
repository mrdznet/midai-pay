package com.midai.pay.settlemoney.service;

import java.util.List;

import com.alibaba.druid.stat.TableStat.Name;
import com.midai.framework.common.BaseService;
import com.midai.pay.settlemoney.entity.BoGetMoney;
import com.midai.pay.settlemoney.query.BoGetMoneyQuery;
import com.midai.pay.settlemoney.vo.SettleCustomerVo;

public interface BoGetMoneyService extends BaseService<BoGetMoney>{

	/**
	 * 清分清算列表
	 */
	 List<SettleCustomerVo> findqueryBoGetMoney(BoGetMoneyQuery query);
	 int findqueryBoGetMoneyCount(BoGetMoneyQuery query);
	 /**
	  * 批量通过
	  */
	 
	 int batchUpdate(String logNos,String name);
}
