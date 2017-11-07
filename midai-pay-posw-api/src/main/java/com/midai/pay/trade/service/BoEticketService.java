package com.midai.pay.trade.service;

import com.midai.framework.common.BaseService;
import com.midai.pay.trade.entity.BoEticket;
import com.midai.pay.trade.vo.EticketReviewVo;

public interface BoEticketService extends BaseService<BoEticket> {

	int saveReviewThrough(String hostTransSsn, String user);
	
	int saveReviewNoThrough(EticketReviewVo reviewVo);
}
