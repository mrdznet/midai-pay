package com.midai.pay.customer.vo;

import java.util.Date;

public class CustomerApplyVo {
	
	private String mercNo;	// 商户编号
	
	private String applyName;	// 申请人
	
	private String title;	// 标题
	
	private String state;	//当前进度
	
	private Date createTime; 	// 申请日期

	
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
