package com.midai.pay.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.user.entity.Bank;
import com.midai.pay.user.mapper.BankMapper;
import com.midai.pay.user.service.BankService;

@Service
public class BankServiceImpl extends BaseServiceImpl<Bank> implements BankService {

	private final BankMapper bankMapper;
	
	public BankServiceImpl(BankMapper bankMapper) {
		super(bankMapper);
		
		this.bankMapper = bankMapper;
	}

	@Override
	public Bank findById(String bankcode) {
		return bankMapper.findById(bankcode);
	}
	
}
