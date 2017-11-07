package com.midai.pay.user.service;

import com.midai.framework.common.BaseService;
import com.midai.pay.user.entity.Bank;

public interface BankService extends BaseService<Bank> {
	
	Bank findById(String bankcode);
	
}
