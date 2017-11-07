package com.midai.pay.handpay.service;

import com.midai.framework.common.po.ResultVal;
import com.midai.pay.handpay.vo.InstiMerchantRequestBean;
import com.midai.pay.handpay.vo.InstiMerchantResponseBean;
import com.midai.pay.handpay.vo.TransQueryRequestBean;
import com.midai.pay.handpay.vo.TransQueryResponseBean;
import com.midai.pay.handpay.vo.WithdrawRequestBean;
import com.midai.pay.handpay.vo.WithdrawResponseBean;

/**
 * 
 * @author Feng
 *
 */
public interface HandpayService {

	// 商户进件
	ResultVal<InstiMerchantResponseBean> instiMerchant(String flag, InstiMerchantRequestBean req);
	
	//T+0 提现申请
	ResultVal<WithdrawResponseBean> t0Withdraw(WithdrawRequestBean req);
	
	//T+0 提现交易查询
	ResultVal<TransQueryResponseBean> transQuery(TransQueryRequestBean req);
	
	public String tranQueryTransQueryResponseBean(TransQueryRequestBean req);
	
	public String t0WithdrawStr(WithdrawRequestBean req);
}
