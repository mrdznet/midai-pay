package com.midai.pay.user.service.impl;

import java.util.List;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.user.entity.BankBranch;
import com.midai.pay.user.mapper.BankBranchMapper;
import com.midai.pay.user.query.BankBranchQuery;
import com.midai.pay.user.service.BankBranchService;

@Service
public class BankBranchImpl extends BaseServiceImpl<BankBranch> implements BankBranchService {

	private final BankBranchMapper bankBranchMapper;
	
	public BankBranchImpl(BankBranchMapper mapper) {
		super(mapper);
		
		this.bankBranchMapper = mapper;
	}

	@Override
	public int pageCount(BankBranchQuery query) {
		return bankBranchMapper.pageCount(query);
	}

	@Override
	public List<BankBranch> pageQuery(BankBranchQuery query) {
		return bankBranchMapper.pageQuery(query);
	}

}
