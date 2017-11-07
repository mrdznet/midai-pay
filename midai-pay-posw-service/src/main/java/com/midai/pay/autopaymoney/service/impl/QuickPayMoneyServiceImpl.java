package com.midai.pay.autopaymoney.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.autopaymoney.entity.BoAutoPayMoney;
import com.midai.pay.autopaymoney.service.QuickPayMoneyService;
import com.midai.pay.autopaymoney.vo.AutoPayMoneyVo;
import com.midai.pay.dealtotal.mapper.DealtotalQuickMapper;

@Service
public class QuickPayMoneyServiceImpl implements QuickPayMoneyService{
	
	@Autowired
	private DealtotalQuickMapper quickMapper;
	
	@Override
	public List<AutoPayMoneyVo> queryPaginationList(Map<String, String> map) {
		
		return quickMapper.queryTransRecordForApp(map);
	}

}
