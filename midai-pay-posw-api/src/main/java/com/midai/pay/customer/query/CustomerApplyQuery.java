package com.midai.pay.customer.query;

import java.io.Serializable;
import java.util.Date;

import com.midai.framework.query.PaginationQuery;

public class CustomerApplyQuery extends PaginationQuery implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mercNo;	// 商户编号
	
	private String applyName;	// 申请人
	
	private Date fromDate; 	// 申请日期
	
	private Date toDate; 	// 申请日期
	
	private String state;	//状态

	public String getMercNo() {
		return mercNo;
	}

	public void setMercNo(String mercNo) {
		this.mercNo = mercNo;
	}

	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}
