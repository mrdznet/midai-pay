package com.midai.pay.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.user.entity.BankBranch;
import com.midai.pay.user.query.BankBranchQuery;

public interface BankBranchMapper extends MyMapper<BankBranch> {
	
	@SelectProvider(type=com.midai.pay.user.provider.BankBranchProvider.class, method="pageCount")
	public int pageCount(BankBranchQuery query);
	
	@SelectProvider(type=com.midai.pay.user.provider.BankBranchProvider.class, method="pageQuery")
	public List<BankBranch> pageQuery(BankBranchQuery query);
	
	@Select(" select branchbankcode , branchbankname, bankcode from tbl_bank_branch where branchbankcode=#{Branchbankcode}")
	BankBranch findByBranchbankcode(String Branchbankcode);
	
}
