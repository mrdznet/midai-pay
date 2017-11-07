package com.midai.pay.mobile.entity;

import com.midai.pay.mobile.AppBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
@EqualsAndHashCode(callSuper=false)
@Data
public class MobileHislogDetailEntity extends AppBaseEntity {


	private String MERCNAM;
	private String MERCID;
	private String TXNTYP;
	private String TXNAMT;
	private String TXNDATE;
	private String ISSINSRESV = "";
	private String TERMAPPID = "";
}
