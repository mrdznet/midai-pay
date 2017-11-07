package com.midai.pay.dealtotal.service;

import java.util.List;

import com.midai.framework.common.BaseService;
import com.midai.pay.dealtotal.entity.DealtotalQuick;
import com.midai.pay.dealtotal.query.DealtotalQuickQuery;
import com.midai.pay.dealtotal.vo.DealtotalQuickQueryVo;
import com.midai.pay.dealtotal.vo.DealtotalQuickStateUpdateVo;
import com.midai.pay.dealtotal.vo.DealtotalQuickVo;
import com.midai.reqbean.QRCodePayReqBean;
import com.midai.resbean.QRCodePayResBean;

public interface DealtotalQuickService extends BaseService<DealtotalQuick>{
	
	/**
	 * 保存交易记录
	 * 
	 * @param vo
	 * @return
	 */
	public boolean save(DealtotalQuickVo vo); 
	
	/**
	 * 更新交易状态
	 * @param vo
	 * @return
	 */
	public String updateState(DealtotalQuickStateUpdateVo vo);
	
	List<DealtotalQuickQueryVo> queryList(DealtotalQuickQuery query);
	
	int queryCount(DealtotalQuickQuery query);

	public Boolean saveOrderData(QRCodePayReqBean qrCodePayReqBean, QRCodePayResBean agentAuthResBean, String mercNo, String mercName, String agentNo, Integer source, Double rate, String remarks);
}
