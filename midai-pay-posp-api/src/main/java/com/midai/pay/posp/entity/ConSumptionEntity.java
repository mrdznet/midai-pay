/**
 * Project Name:midai-pay-posp-api
 * File Name:ConSumptionEntity.java
 * Package Name:com.midai.pay.posp.entity
 * Date:2016年9月23日下午2:00:02
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.posp.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName:ConSumptionEntity <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年9月23日 下午2:00:02 <br/>
 * 
 * @author 陈勋
 * @version
 * @since JDK 1.7
 * @see
 */
@Data
public class ConSumptionEntity implements Serializable {

	private String TRACK;

	private String TSEQNO;

	private String CTXNAT;

	private String TPINBLK;

	private String PCSIM;

	private String CRDNO;

	private String TRANCODE;

	private String PHONENUMBER;

	private String CHECKX;

	private String APPTOKEN;

	private String CHECKY;

	private String TERMINALNUMBER;

	private String TTXNTM;

	private String TTXNDT;

	private String PSAMCARDNO;

	private String MAC;

	private String IDFID;

	private String ELESIGNA;

	private String SPECIALFEETYPE;

	private String CRDSQN;

	private String ICDAT;

	private String InMod;
	
	private String cardValidDate;
}
