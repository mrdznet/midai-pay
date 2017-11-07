package com.midai.pay.user.query;

import java.io.Serializable;

import com.midai.framework.query.PaginationQuery;

public class BankBranchQuery extends PaginationQuery implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String bankcode;

	private String branchbankcode;
	
	private String branchbankname;

	public String getBankcode() {
		return bankcode;
	}

	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}

	public String getBranchbankcode() {
		return branchbankcode;
	}

	public void setBranchbankcode(String branchbankcode) {
		this.branchbankcode = branchbankcode;
	}

	public String getBranchbankname() {
		return branchbankname;
	}

	public void setBranchbankname(String branchbankname) {
		this.branchbankname = branchbankname;
	}
	
	
}
