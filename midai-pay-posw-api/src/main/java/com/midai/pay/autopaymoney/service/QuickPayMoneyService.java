package com.midai.pay.autopaymoney.service;

import java.util.List;
import java.util.Map;

import com.midai.pay.autopaymoney.entity.BoAutoPayMoney;
import com.midai.pay.autopaymoney.vo.AutoPayMoneyVo;

public interface QuickPayMoneyService{
	
	List<AutoPayMoneyVo> queryPaginationList(Map<String, String> map);
}
