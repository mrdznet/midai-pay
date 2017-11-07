package com.midai.pay.inst.query;

import java.io.Serializable;

import lombok.Data;

@Data
public class InstSimpleVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String instCode;
	private String instName;
}
