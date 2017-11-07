package com.midai.pay.mobile.entity;

import com.midai.pay.mobile.AppBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
@EqualsAndHashCode(callSuper=false)
@Data
public class CustomerDeviceEntity extends AppBaseEntity {

	private String UNID;
	private String MERCID;
	private String MERCNAME;
	private String STATE;
	private String OPSTATE;
	private String DEVICENO;
	private String DEVICETYPE;
	private String ISFIRST;
	
}
