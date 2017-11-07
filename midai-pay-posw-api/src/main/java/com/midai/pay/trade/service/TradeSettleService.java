package com.midai.pay.trade.service;

import java.util.List;

import com.midai.framework.common.BaseService;
import com.midai.pay.settlemoney.entity.BoGetMoney;
import com.midai.pay.settlemoney.query.BoGetMoneyQuery;
import com.midai.pay.settlemoney.vo.SettleCustomerVo;
import com.midai.pay.trade.vo.TradeEticketVo;

public interface TradeSettleService extends BaseService<BoGetMoney> {

	/**
	 * 交易结算列表查询
	 */
	List<SettleCustomerVo> findqueryToTrade(BoGetMoneyQuery query);
	int findqueryToTradeCount(BoGetMoneyQuery query);
	/**
	 * 通过
	 */
	int bacthUpdateCheckState(String lognos, String user);
	
	/**
	 * 详情
	 */
	
	List<TradeEticketVo> getPageDetailList(String mercId);
}
