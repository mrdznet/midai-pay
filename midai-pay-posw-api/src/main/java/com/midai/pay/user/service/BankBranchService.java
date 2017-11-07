package com.midai.pay.user.service;

import java.util.List;

import com.midai.framework.common.BaseService;
import com.midai.pay.user.entity.BankBranch;
import com.midai.pay.user.query.BankBranchQuery;

public interface BankBranchService extends BaseService<BankBranch> {

	int pageCount(BankBranchQuery query);
	
	List<BankBranch> pageQuery(BankBranchQuery query);
	
}
