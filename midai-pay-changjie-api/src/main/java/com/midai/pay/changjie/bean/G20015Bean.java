package com.midai.pay.changjie.bean;


/**
 * G50001接口 Bean
 * @author Thinker
 *
 */
public class G20015Bean extends Gw01MsgBase{
	
	
	private String accountNo;//待查账号
	private String accountName;//待查户名
	
	private String usableBalance;//可用余额
	private String totalBalance;//总余额
	private String frozenBalance;//冻结余额
	private String confirm_balance;//待确认金额
	private String bodyErrMsg;
	private String bodyRetCode;
	
	public String getBodyErrMsg() {
		return bodyErrMsg;
	}
	public void setBodyErrMsg(String bodyErrMsg) {
		this.bodyErrMsg = bodyErrMsg;
	}
	public String getBodyRetCode() {
		return bodyRetCode;
	}
	public void setBodyRetCode(String bodyRetCode) {
		this.bodyRetCode = bodyRetCode;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getUsableBalance() {
		return usableBalance;
	}
	public void setUsableBalance(String usableBalance) {
		this.usableBalance = usableBalance;
	}
	public String getTotalBalance() {
		return totalBalance;
	}
	public void setTotalBalance(String totalBalance) {
		this.totalBalance = totalBalance;
	}
	public String getFrozenBalance() {
		return frozenBalance;
	}
	public void setFrozenBalance(String frozenBalance) {
		this.frozenBalance = frozenBalance;
	}
	public String getConfirm_balance() {
		return confirm_balance;
	}
	public void setConfirm_balance(String confirm_balance) {
		this.confirm_balance = confirm_balance;
	}
	
	
}
