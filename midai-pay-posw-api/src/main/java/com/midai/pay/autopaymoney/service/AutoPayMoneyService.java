package com.midai.pay.autopaymoney.service;

import java.util.List;
import java.util.Map;

import com.midai.framework.common.BaseService;
import com.midai.pay.autopaymoney.entity.BoAutoPayMoney;
import com.midai.pay.autopaymoney.query.BoAutoPayMoneyQuery;
import com.midai.pay.autopaymoney.vo.AutoPayMoneyVo;

public interface AutoPayMoneyService extends BaseService<BoAutoPayMoney> {

	/**
	 * 自动打款结果查询
	 */
	List<AutoPayMoneyVo> findQueryAutoPayMoney1(BoAutoPayMoneyQuery query);
	int findQueryAutoPayMoneyCount1(BoAutoPayMoneyQuery query);
	/**
	 * 打款结果查询
	 */
	List<AutoPayMoneyVo>  findQueryAutoPayMoney(BoAutoPayMoneyQuery query);
	int findQueryAutoPayMoneyCount(BoAutoPayMoneyQuery query);
	
	/**
	 * 自动打款
	 * @param logon
	 * @param dfway
	 * @param currUser
	 * @return
	 */
	int autoPay(String logon, String dfway, String currUser);

	
	/**
	 * excel下载
	 */
	List<AutoPayMoneyVo>  excelQueryAutoPayMoney(BoAutoPayMoneyQuery query);
	int excelQueryAutoPayMoneyCount(BoAutoPayMoneyQuery query);
	
	List<AutoPayMoneyVo> queryPaginationList(Map<String, String> map);
	/**
	 * 所有到帐记录查询
	 * @param map
	 * @return
	 */
	List<AutoPayMoneyVo> queryPaginationWithQuickPayList(Map<String, String> map);
	
	long queryTotalMoneyWithDate(Map<String, String> map);
	/**
	 * 打款查询
	 * 
	 */
	void payQuery();
	
	/**
	 * 打款失败查询
	 */
	List<AutoPayMoneyVo>  findErrList(BoAutoPayMoneyQuery query);
	int findErrListCount(BoAutoPayMoneyQuery query);
	
	/**
	 * 更新打款状态
	 * @param logon
	 * @param state
	 * @return
	 */
	void signPay(String logon, boolean isSuccess);
}
