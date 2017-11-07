package com.midai.pay.trade.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.framework.common.PageEnum;
import com.midai.pay.mobile.utils.MassageUtil;
import com.midai.pay.settlemoney.entity.BoGetMoney;
import com.midai.pay.settlemoney.mapper.BoGetMoneyMapper;
import com.midai.pay.settlemoney.query.BoGetMoneyQuery;
import com.midai.pay.settlemoney.vo.SettleCustomerVo;
import com.midai.pay.trade.mapper.TradeSettleMapper;
import com.midai.pay.trade.service.TradeSettleService;
import com.midai.pay.trade.vo.TradeEticketVo;
import com.midai.pay.user.service.SystemUserService;

@Service
public class TradeSettleServiceImpl extends BaseServiceImpl<BoGetMoney> implements TradeSettleService{

	private final BoGetMoneyMapper boGetMoneyMapper;
	public TradeSettleServiceImpl(BoGetMoneyMapper mapper) {
		super(mapper);
		this.boGetMoneyMapper=mapper;
	}

	@Autowired
	private TradeSettleMapper tradeSettleMapper;
	
	@Autowired
	private MassageUtil massageUtil;

	@Autowired
	private SystemUserService systemUserService;
	
	@Override
	public List<SettleCustomerVo> findqueryToTrade(BoGetMoneyQuery query) {
		return boGetMoneyMapper.findqueryToTrade(query);
	}

	@Override
	public int findqueryToTradeCount(BoGetMoneyQuery query) {
		return boGetMoneyMapper.findqueryToTradeCount(query);
	}

	@Override
	public int bacthUpdateCheckState(String lognos, String user) {
		massageUtil.sendMsgByResource(PageEnum.qfqs.toString(), systemUserService.getInscode(user)); //发消息给清分清算
		
		return boGetMoneyMapper.bacthUpdateCheckState(lognos);
	}

	@Override
	public List<TradeEticketVo> getPageDetailList(String mercId) {
		return tradeSettleMapper.getPageDetailList(mercId);
	}

}
