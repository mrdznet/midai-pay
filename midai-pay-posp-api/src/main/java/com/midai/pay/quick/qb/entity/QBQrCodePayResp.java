package com.midai.pay.quick.qb.entity;

import java.io.Serializable;

public class QBQrCodePayResp  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** 发送时间*/
	private String sendTime;
	/** 发送流水号*/
	private String sendSeqId;
	/** 交易类型码*/
	private String transType;
	/**机构号*/
	private String organizationId;
	/**二维码*/
	private String imgUrl;
	/**交易金额*/
	private String transAmt;
	/**应答码*/
	private String respCode;
	/**应答码描述*/
	private String respDesc;
	/**报文鉴别码*/
	private String mac;
	
	public String getSendTime() {
		return sendTime;
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
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getTransAmt() {
		return transAmt;
	}
	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespDesc() {
		return respDesc;
	}
	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}

}
