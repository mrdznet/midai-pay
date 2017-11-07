package com.midai.pay.trade.service;

import java.util.List;

import com.midai.framework.common.BaseService;
import com.midai.pay.mobile.vo.EticketSignVo;
import com.midai.pay.trade.entity.DealTotal;
import com.midai.pay.trade.query.TradeReviewQuery;
import com.midai.pay.trade.vo.TradeReviewVo;

public interface TradeReviewService extends BaseService<DealTotal> {
	
	List<TradeReviewVo> findQuery(TradeReviewQuery query);
	
	int findQueryCount(TradeReviewQuery query);
	
	int countReSign(String mobile);
	
	List<EticketSignVo> findAllReSign(String mobile);
	
	boolean tickReSign(String logn, String mobile);
	
	List<TradeReviewVo> channelEticketQuery(TradeReviewQuery query, String user);
	int channelEticketCount(TradeReviewQuery query);
	
	String eticketUrl(String logon);
}
