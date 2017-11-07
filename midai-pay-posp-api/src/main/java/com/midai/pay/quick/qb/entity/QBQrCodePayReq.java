package com.midai.pay.quick.qb.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QBQrCodePayReq  implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 发送时间*/
	private String sendTime;
	/** 发送流水号*/
	private String sendSeqId;
	/** 交易类型码*/
	private String transType;
	/**机构号*/
	private String organizationId;
	/**支付通道*/
	private String payPass;
	/**交易金额*/
	private String transAmt;
	/**手续费*/
	private String fee;
	/**结算卡号*/
	private String cardNo;
	/**持卡人姓名*/
	private String name;
	/**持卡人身份证号*/
	private String idNum;
	/**商品描述*/
	private String body;
	/**通知地址*/
	private String notifyUrl;
	/**收款方手机号*/
	private String mobile;
	/**报文鉴别码*/
	private String mac;
	
	public String getSendTime() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sf.format(new Date());
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getSendSeqId() {
		return sendSeqId;
	}
	public void setSendSeqId(String sendSeqId) {
		this.sendSeqId = sendSeqId; 
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public String getPayPass() {
		return payPass;
	}
	public void setPayPass(String payPass) {
		this.payPass = payPass;
	}
	public String getTransAmt() {
		return transAmt;
	}
	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	
}
