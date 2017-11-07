package com.midai.pay.autopaymoney.service;

public interface JuZhenAutoHitPayService {
	/**
	 * 
	 * @Title: jzpayamt @Description: TODO(这里用一句话描述这个方法的作用) @param @param
	 *         orderid @param @param accountno @param @param
	 *         accountname @param @param tixianamt @param @param
	 *         bankname @param @param mercid @param @param
	 *         hosttransssn @param @param tradeCode @param @param
	 *         searchCode @param @return 设定文件 @return String 返回类型 @throws
	 */
	public String jzpayamt(String orderid, String accountno, String accountname, String tixianamt,
			String bankname, String mercid, String hosttransssn, String tradeCode, String searchCode);
}
