package com.midai.pay.mobile.entity;

import com.midai.pay.mobile.AppBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
@EqualsAndHashCode(callSuper=false)
@Data
public class UserRegisterEntity extends AppBaseEntity {

	private String PHONENUMBER;
	private String USERNAME;
	private String IDNUMBER;
	private String MERNAME;
	private String SCOBUS;
	private String MERADDRESS;
	private String ACCOUNTNAME;
	private String ACCOUNTNO;
	private String PROVINCEID;
	private String CITYID;
	private String BANKID;
	private String BRANCHBANKNAME;
	private String TERMNO;
	private String MERAUTO;
	private String TYPE;
	private String PASSWORD;
	private String CPASSWORD;
	private String MSGCODE;
	private String OLDPASSWORD;
	private String NEWPASSWORD;
	private String IP;
	private String PARENTDIR;
	private String URL1;
	private String URL2;
	private String URL3;
	private String URL4;
	private String URL5;
	private String URL6;
	private String URL7;
	private String URL8;
	private String INSCODE;
	private String PARCOD;
	private String LOGNO;
	private String os;
	private String version;
	private String DEVICENO;
	private String TERMID;
	private String TRANSNO;
	private String TRANSDATE;
	private String sign_type;
	private String MERTYPE;
	
}
