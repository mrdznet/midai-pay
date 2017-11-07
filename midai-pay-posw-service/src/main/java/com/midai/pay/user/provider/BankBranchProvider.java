package com.midai.pay.user.provider;

import org.apache.commons.lang3.StringUtils;

import com.midai.pay.user.query.BankBranchQuery;

public class BankBranchProvider {
	
	public String columns = " branchbankcode, branchbankname, bankcode ";
	
	public String pageCount(BankBranchQuery query){
		StringBuffer sb = new StringBuffer(" SELECT COUNT(1) FROM tbl_bank_branch WHERE 1=1 "); 
		
		if(null != query){
			if(StringUtils.isNotBlank(query.getBankcode())){
				sb.append(" AND bankcode= #{bankcode}");
			}
			if(StringUtils.isNotBlank(query.getBranchbankcode())){
				sb.append(" AND branchbankcode LIKE '%" +query.getBranchbankcode()+ "%' ");
			}
			
			if(StringUtils.isNotBlank(query.getBranchbankname())){
				sb.append(" AND branchbankname LIKE '%" +query.getBranchbankname()+ "%' ");
			}
		}
		
		return sb.toString();
	}
	
	public String pageQuery(BankBranchQuery query){
		StringBuffer sb = new StringBuffer(" SELECT " + columns + " FROM tbl_bank_branch WHERE 1=1 ");
		
		if(null != query){
			
			if(StringUtils.isNotBlank(query.getBankcode())){
				sb.append(" AND bankcode= #{bankcode}");
			}
			
			if(StringUtils.isNotBlank(query.getBranchbankcode())){
				sb.append(" AND branchbankcode LIKE '%" +query.getBranchbankcode()+ "%' ");
			}
			
			if(StringUtils.isNotBlank(query.getBranchbankname())){
				sb.append(" AND branchbankname LIKE '%" +query.getBranchbankname()+ "%' ");
			}
			
			/*if (StringUtils.isNotBlank(query.getOrderBy())) {
				sb.append(" order by id " + query.getOrderBy());
			} else {
				sb.append(" order by id desc ");
			}*/
			
			if (query.getPageSize() > 0) {
				sb.append(" limit "+ (query.getPageNumber()-1)*query.getPageSize()+","+query.getPageSize());
			}
		}
		
		return sb.toString();
	}
}
