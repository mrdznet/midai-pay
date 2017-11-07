package com.midai.pay.quick.qb.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class QBQrCodeResultEntity  implements Serializable{

	private static final long serialVersionUID = 1L;

	private String RSPCOD;

	private String RSPMSG;
	
	private String IMGURL;
}
