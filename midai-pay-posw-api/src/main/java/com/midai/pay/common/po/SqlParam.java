package com.midai.pay.common.po;

import java.io.Serializable;

import lombok.Data;

@Data
public class SqlParam implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String param_1;
	private String param_2;
	private String param_3;
	
	private Integer param_4;
}
