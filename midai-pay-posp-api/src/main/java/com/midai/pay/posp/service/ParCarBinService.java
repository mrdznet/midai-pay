package com.midai.pay.posp.service;

import com.midai.pay.posp.entity.ParCardBin;

public interface ParCarBinService {

	/**
	 * 获取卡信息
	 * @param cardNo
	 * @return
	 */
	public ParCardBin findByCardNo(String cardNo);
	
	/**
	 * 判断是否为借记卡
	 * @param cardNo 卡号
	 * @return
	 */
	public Boolean isDebiCard (String cardNo);
}
