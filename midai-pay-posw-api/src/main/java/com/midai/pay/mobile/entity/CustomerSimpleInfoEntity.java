package com.midai.pay.mobile.entity;

import java.util.List;

import com.midai.pay.mobile.AppBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
@EqualsAndHashCode(callSuper=false)
@Data
public class CustomerSimpleInfoEntity extends AppBaseEntity {

	private String MERCNAME;
	private String MERCID;
	private String ACCOUNTNO;
	private String ACCOUNTNAME;
	private String BRANCHBANKNAME;
	private String BALANCE;
	private String STATE;
	private String OPSTATE;
	private String SUPERSTATE;
	private String ACCOUNTSTATE;
	private String MERAUTO;
	private String HFROZENAMT;
	private String TOTALABLEAMT;
	private String TOTALFROZENAMT;
	private String SIGNNUM;
	private String PROBABLEAMT;
	private String MERLEV;
	private List DEVICES;
}
