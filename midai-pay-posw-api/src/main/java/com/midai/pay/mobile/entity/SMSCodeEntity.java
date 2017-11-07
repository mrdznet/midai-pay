package com.midai.pay.mobile.entity;

import com.midai.pay.mobile.AppBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
@EqualsAndHashCode(callSuper=false)
@Data
public class SMSCodeEntity extends AppBaseEntity {

	private String PHONENUMBER;
	private String SMSCODE;
	private String SMSCONTENT;
	private String CREATETIMES;
	private String STYPE;
	private String STATE;
	
}
