package com.midai.pay.mobile.entity;

import com.midai.pay.mobile.AppBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
@EqualsAndHashCode(callSuper=false)
@Data
public class AuthUserInforEntity extends AppBaseEntity {

	private String STATUS = "0";
	private String MERCNAME = "" ;
	private String ACCOUNTNAME = "";
	private String SCOBUS = "";
	private String ADDRESS = "" ;
	private String BANKNAME = "" ;
	private String BANKCODE = "" ;
	private String BRANCHBANKNAME = "" ;
	private String ACCOUNTNO = "" ;
	private String IDCARD = "" ;						
	private String TERMNO = "" ;
	private String CITYCODE = "" ;
	private String CITYNAME = "" ;
	private String PROVINCECODE = "" ;
	private String PROVINCENAME = "" ;
	private String OPSTATE = "0" ;
	private String MERAUTO = "0" ;
	private String BUSINESSLICENSE = "" ;
	private String ORGANIZATIONNO = "" ;
	private String TAXCERTIFICATENO = "";
	private String AGETMERTYPE = "";
	
}
