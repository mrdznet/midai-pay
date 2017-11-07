package com.midai.pay.dealtotal.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class DealtotalQuickStateUpdateVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/* 交易流水 */
	private String seqId;
	
	/* 支付状态 */
	private String payDesc;
	
	/* 支付状态码 */
	private String payResult;
	
	/* 代付状态   */
	private String t0RespDesc;
	
	/* 代付状态码   */
	private String t0RespCode;
}
