package com.midai.pay.mobile.vo;

import lombok.Data;

@Data
public class EticketSignVo {

	private String LOGNO;
	private String DATETIME;
	private String TRANSCODENAME;
	private String TRANSAMT;
	private String CARDNO;
	private String PICPATH;
	private String BANKNAME;
	private String REASON;
}
