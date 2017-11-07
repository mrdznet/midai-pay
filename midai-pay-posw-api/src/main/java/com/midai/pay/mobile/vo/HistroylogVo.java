package com.midai.pay.mobile.vo;

import java.util.Date;

import lombok.Data;

@Data
public class HistroylogVo {

	private Date transrecvdate;
	private String mchntcodein;
	private String mercname;
	private String hosttransssn;
	private String transcodename;
	private String transamt;
	private String transcode;
	private String transst;
	private String priacctno;
	private String respcdloc;
	private String respcdlocdsp;
}
