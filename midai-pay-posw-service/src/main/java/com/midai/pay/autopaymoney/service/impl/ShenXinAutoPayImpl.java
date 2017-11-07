package com.midai.pay.autopaymoney.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.autopaymoney.entity.BoAutoPayMoney;
import com.midai.pay.autopaymoney.mapper.AutoPayMoneyMapper;
import com.midai.pay.autopaymoney.service.ShenXinAutoPayService;
import com.midai.pay.settlemoney.service.BoGetMoneyService;
import com.midai.pay.trade.service.BoEticketService;
import com.midai.pay.trade.service.TradeSettleService;

@Service
public class ShenXinAutoPayImpl implements ShenXinAutoPayService{
	
	private Logger logger = LoggerFactory.getLogger(ShenXinAutoPayImpl.class);
	
	@Autowired
	private BoEticketService boEticketServce;
	
	@Autowired
	private TradeSettleService tradeSettleService;
	
	@Autowired
	private BoGetMoneyService boGetMoneyService;
	
	private final AutoPayMoneyMapper autoPayMoneyMapper;
	
	public ShenXinAutoPayImpl(AutoPayMoneyMapper apmm){
		super();
		this.autoPayMoneyMapper = apmm;
	}

	@Override
	@Transactional(isolation=Isolation.SERIALIZABLE)
	public void autoPay(String txno, String channel){
		final String user = "admin";
		
		logger.info("打款数据自动录入开始："+txno);
		
		boEticketServce.saveReviewThrough(txno, user);
		tradeSettleService.bacthUpdateCheckState(txno,user);
		boGetMoneyService.batchUpdate(txno, user);
		

		// 更新打款表
		BoAutoPayMoney payMoney = new BoAutoPayMoney();
		payMoney.setTixiLogno(txno);
		payMoney.setPayTime(new Date());
		payMoney.setPayPerson(user);
		//更新打款结果信息
		payMoney.setPayState(3);
		payMoney.setChannelCode("-");
		payMoney.setPayChannel(channel);
		payMoney.setErrorCode("00");
		payMoney.setErrorMsg("收单方代付");
		//更新银行信息
		payMoney.setBankName("-");
		payMoney.setAccountName("-");
		payMoney.setAccountNo("-");
		payMoney.setBankCode("-");
		
		autoPayMoneyMapper.updatePaySettleForShenXin(payMoney);
		
		logger.info("打款数据自动录入结束："+txno);
	}

}
